package com.example.coffeeorderjavaapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeorderjavaapp.adapter.CheckoutProductAdapter;
import com.example.coffeeorderjavaapp.fragment.CartFragment;
import com.example.coffeeorderjavaapp.model.CartItem;
import com.example.coffeeorderjavaapp.model.Order;
import com.example.coffeeorderjavaapp.model.OrderProduct;
import com.example.coffeeorderjavaapp.model.Product;
import com.example.coffeeorderjavaapp.model.ToppingOption;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String user_id = FirebaseAuth.getInstance().getUid();
    private ArrayList<OrderProduct> orderProducts = new ArrayList<>();
    private TextView deliveryTv;
    private String delivery_location;
    private CheckoutProductAdapter adapterRef;
    private int shippingFee = 35000;
    private int subTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        deliveryTv = findViewById(R.id.deliveryLocationTv);
        TextView checkoutTotalTv = findViewById(R.id.checkoutTotalTv);
        TextView checkoutTaxTv = findViewById(R.id.checkoutTaxTv);
        TextView checkoutShippingFeeTv = findViewById(R.id.checkoutShippingFeeTv);
        TextView checkoutSubTotalTv = findViewById(R.id.checkoutSubTotalTv);

        checkoutShippingFeeTv.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(shippingFee));
        Button openMapBtn = findViewById(R.id.openMapBtn);
        Button backBtn = findViewById(R.id.checkoutBackBtn);
        Button checkoutBtn = findViewById(R.id.checkoutButton);

        openMapBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        });

        backBtn.setOnClickListener(v -> {
            finish();
        });

        checkoutBtn.setOnClickListener(v -> {
            if (delivery_location == null) {
                Toast.makeText(this, "Chưa chọn địa điểm nhận hàng", Toast.LENGTH_SHORT).show();
                return;
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Date currentTime = Calendar.getInstance().getTime();
                Order order = new Order(delivery_location, this.orderProducts, "Thanh toán khi nhận hàng", currentTime, user_id);
                db.collection("orders").add(order).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(this, "Tạo đơn hàng thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                    }
                    else {
                        Toast.makeText(this, "Tạo đơn hàng thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        RecyclerView productRCV = findViewById(R.id.checkoutProductRecView);
        fetchCartItem(cartItems -> {
            for (CartItem cartItem : cartItems) {
                this.orderProducts.add(new OrderProduct(cartItem.getProduct(), cartItem.getTopping_options(), cartItem.getSize_option(), cartItem.getQuantity()));
            }

            int total = countTotal(cartItems);
            int tax = total * 10 / 100;
            subTotal = total + tax + shippingFee;

            checkoutTotalTv.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(total));
            checkoutTaxTv.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(tax));
            checkoutSubTotalTv.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(subTotal));

            CheckoutProductAdapter productAdapter = new CheckoutProductAdapter(getApplicationContext(), cartItems);
            productRCV.setAdapter(productAdapter);
            adapterRef = productAdapter;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            productRCV.setLayoutManager(linearLayoutManager);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            String deliveryLocation = extras.getString("deliveryLocation");
            this.delivery_location = deliveryLocation;
            this.deliveryTv.setText("Tới: " + deliveryLocation);
        }
    }

    private int countTotal(ArrayList<CartItem> cartItems) {
        int total = 0;
        for (CartItem cartItem : cartItems) {
            int productPrice = cartItem.getProduct().getPrice();
            int optionPrice = countOptionPrice(cartItem);
            int quantity = cartItem.getQuantity();
            total += (productPrice + optionPrice) * quantity;
        }
        return total;

    }

    private int countOptionPrice(CartItem cartItem) {
        int sizePrice = 0;
        int toppingPrice = 0;
        if (cartItem.getTopping_options() != null) {
            for (ToppingOption toppingOption : cartItem.getTopping_options()) {
                toppingPrice += toppingOption.getTopping_price();
            }
        }

        if (cartItem.getSize_option() != null) {
            sizePrice = cartItem.getSize_option().getSize_price();
        }

        return sizePrice + toppingPrice;
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

    private interface FetchCartItemCallback {
        void onCallBack(ArrayList<CartItem> cartItems);
    }
}