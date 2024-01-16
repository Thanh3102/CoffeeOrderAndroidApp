package com.example.coffeeorderjavaapp.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeorderjavaapp.CheckoutActivity;
import com.example.coffeeorderjavaapp.R;
import com.example.coffeeorderjavaapp.adapter.CartAdapter;
import com.example.coffeeorderjavaapp.model.CartItem;
import com.example.coffeeorderjavaapp.model.Product;
import com.example.coffeeorderjavaapp.model.ToppingOption;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartFragment extends Fragment {

    public CartFragment() {
        // Required empty public constructor
    }

    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private CartAdapter cartAdapterRef;
    private static TextView totalTv;

    private ArrayList<CartItem> cartItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart, container, false);
        totalTv = root.findViewById(R.id.cartItemTotalTv);
        RecyclerView cartRecV = root.findViewById(R.id.cartRecycleView);

        fetchCartItem((cartItems) -> {
            this.cartItems = cartItems;
            CartAdapter cartAdapter = new CartAdapter(getContext(), totalTv, cartItems);
            cartRecV.setAdapter(cartAdapter);
            cartAdapterRef = cartAdapter;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            cartRecV.setLayoutManager(linearLayoutManager);
            int total = countTotal(cartItems);
            Log.e("TOTAL", String.valueOf(total));
            totalTv.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(total));
        });

        TextView deleteAllTv = root.findViewById(R.id.cartDeleteAllBtn);
        deleteAllTv.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext()).setTitle("Xác nhận xóa?").setMessage("Hành động này sẽ không thể hoàn tác")
                    .setPositiveButton("Đồng ý", (dialog, which) -> {
                        db.collection("carts")
                                .whereEqualTo("user_id", user_id)
                                .get().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        WriteBatch batch = db.batch();
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            batch.delete(db.collection("carts").document(document.getId()));
                                        }
                                        batch.commit().addOnSuccessListener(unused -> {
                                            Snackbar.make(root, "Đã xóa tất cả sản phẩm trong giỏ hàng", Snackbar.LENGTH_SHORT).show();
                                            cartAdapterRef.clearList();
                                        }).addOnFailureListener(e -> {
                                            Toast.makeText(getContext(), "Đã có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                                        });
                                    } else {
                                        Log.d("DELETE ALL ERROR", "Error getting documents: ", task.getException());
                                    }
                                });
                    })
                    .setNegativeButton("Hủy", null).show();
        });

        MaterialButton toCheckoutBtn = root.findViewById(R.id.toCheckOutBtn);
        toCheckoutBtn.setOnClickListener(v -> {
            if (this.cartItems.size() != 0) {
                Intent intent = new Intent(getContext(), CheckoutActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(this.getContext(), "Không có sản phẩm trong giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchCartItem(cartItems -> {
            this.cartItems = cartItems;
            int total = countTotal(cartItems);
            totalTv.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(total));
            cartAdapterRef.changeList(cartItems);
        });
    }

    private int countTotal(ArrayList<CartItem> cartItems) {
        int total = 0;
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            int optionPrice = countOptionPrice(cartItem);
            int productPrice = product.getPrice();
            int quantity = cartItem.getQuantity();
            total += (optionPrice + productPrice) * quantity;
        }
        return total;
    }

    private void fetchCartItem(FetchCartItemCallback fetchCartItemCallback) {
        db.collection("carts").whereEqualTo("user_id", user_id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<CartItem> cartItems = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    CartItem cartItem = doc.toObject(CartItem.class);
                    cartItem.setId(doc.getId());
                    cartItems.add(cartItem);
                }
                fetchCartItemCallback.onCallBack(cartItems);
            }
        });
    }

    private int countOptionPrice(CartItem cartItem) {
        int total = 0;
        if (cartItem.getTopping_options() != null) {
            for (ToppingOption toppingOption : cartItem.getTopping_options()) {
                total += toppingOption.getTopping_price();
            }
        }
        if (cartItem.getSize_option() != null) {
            total += cartItem.getSize_option().getSize_price();
        }
        return total;
    }

    private interface FetchCartItemCallback {
        void onCallBack(ArrayList<CartItem> cartItems);
    }
}