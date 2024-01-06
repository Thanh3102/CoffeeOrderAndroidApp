package com.example.coffeeorderjavaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeorderjavaapp.adapter.SizeOptionAdapter;
import com.example.coffeeorderjavaapp.adapter.ToppingOptionAdapter;
import com.example.coffeeorderjavaapp.model.Product;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        String productId = getIntent().getStringExtra("productId");
        TextView nameTv = findViewById(R.id.detailProductName);
        TextView categoryTv = findViewById(R.id.detailProductCategory);
        TextView descTv = findViewById(R.id.detailProductDesc);
        TextView priceTv = findViewById(R.id.productDetailPrice);
        TextView quantityTv = findViewById(R.id.productDetailQuantityTv);
        Button backBtn = findViewById(R.id.productDetailBackBtn);
        Button addBtn = findViewById(R.id.productDetailAddCartBtn);
        Button decreaseQtyBtn = findViewById(R.id.detailProductDecreaseQtyBtn);
        Button increaseQtyBtn = findViewById(R.id.detailProductIncreaseQtyBtn);
        ImageView imageView = findViewById(R.id.detailProductImage);
        RecyclerView sizeRecyclerView = findViewById(R.id.detailProductSizeRV);
        RecyclerView toppingRecycleView = findViewById(R.id.detailProductToppingRV);

        backBtn.setOnClickListener(v -> {
            finish();
        });


        if (productId == null) {
            Intent intent = new Intent(this, MainActivity.class);
            Toast.makeText(this, "No Product Id", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("products").document(productId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Product product = document.toObject(Product.class);
                    if (product.getSize_options() != null) {
                        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(ProductDetailActivity.this);
                        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
                        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
                        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
                        sizeRecyclerView.setLayoutManager(flexboxLayoutManager);
                        SizeOptionAdapter sizeAdapter = new SizeOptionAdapter(ProductDetailActivity.this, product.getSize_options());
                        sizeRecyclerView.setAdapter(sizeAdapter);
                    } else {
                        findViewById(R.id.productDetailSizeSection).setVisibility(View.GONE);
                    }

                    if (product.getTopping_options() != null) {
                        ToppingOptionAdapter toppingAdapter = new ToppingOptionAdapter(ProductDetailActivity.this, product.getTopping_options());
                        toppingRecycleView.setAdapter(toppingAdapter);
                    } else {
                        findViewById(R.id.productDetailToppingSection).setVisibility(View.GONE);
                    }
                    Picasso.with(ProductDetailActivity.this).load(product.getImageURL()).into(imageView);
                    nameTv.setText(product.getName());
                    categoryTv.setText(product.getCategory());
                    descTv.setText(product.getDescription());
                    priceTv.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(product.getPrice()));
                }
            } else {
                Log.e("Error", "fail with", task.getException());
            }
        });

        addBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Add to cart function", Toast.LENGTH_SHORT).show();
            finish();
        });

        increaseQtyBtn.setOnClickListener(v -> {
            int current = Integer.parseInt(quantityTv.getText().toString());
            if(current != 99){
                quantityTv.setText(String.valueOf(current+1));
            }
            else{
                Toast.makeText(this, "Số lượng đã đạt tối đa", Toast.LENGTH_SHORT).show();
            }
        });

        decreaseQtyBtn.setOnClickListener(v -> {
            int current = Integer.parseInt(quantityTv.getText().toString());
            if(current != 1){
                quantityTv.setText(String.valueOf(current-1));
            }
            else{
                Toast.makeText(this, "Số lượng nhỏ nhất là 1", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
