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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartFragment extends Fragment{

    public CartFragment() {
        // Required empty public constructor
    }
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private final ArrayList<CartItem> cartItems = new ArrayList<>();
    private CartAdapter cartAdapter;

    private TextView totalTv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart, container, false);
        this.totalTv = root.findViewById(R.id.cartItemTotalTv);
        db.collection("carts").whereEqualTo("user_id", this.user_id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    CartItem cartItem = doc.toObject(CartItem.class);
                    cartItem.setId(doc.getId());
                    this.cartItems.add(cartItem);
                }
                RecyclerView cartRecV = root.findViewById(R.id.cartRecycleView);
                CartAdapter cartAdapter = new CartAdapter(this.getContext(), totalTv, this.cartItems);
                cartRecV.setAdapter(cartAdapter);
                this.cartAdapter = cartAdapter;
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
                cartRecV.setLayoutManager(linearLayoutManager);

                int total = this.cartAdapter.getTotalPrice();
                this.totalTv.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(total));
            }
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
                                            this.cartAdapter.clearList();
                                            this.totalTv.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(this.cartAdapter.getTotalPrice()));
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
            Intent intent = new Intent(getContext(), CheckoutActivity.class);
            startActivity(intent);
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("carts").whereEqualTo("user_id", this.user_id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<CartItem> newList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    CartItem cartItem = doc.toObject(CartItem.class);
                    cartItem.setId(doc.getId());
                    newList.add(cartItem);
                }
                this.cartAdapter.changeList(newList);
                this.totalTv.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(this.cartAdapter.getTotalPrice()));
            }
        });
    }

}