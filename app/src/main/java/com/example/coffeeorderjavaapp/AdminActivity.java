package com.example.coffeeorderjavaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.coffeeorderjavaapp.adapter.CategoryAdapter;
import com.example.coffeeorderjavaapp.adapter.ProductManagerAdapter;
import com.example.coffeeorderjavaapp.model.Product;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
    public AdminActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Button btnSignOut = findViewById(R.id.btnSignOut);
        Button addProduct = findViewById(R.id.btnManageProducts);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<Product> productManagerFromDatabase = new ArrayList<>();
        db.collection("products").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Product product = document.toObject(Product.class);
                    product.setId(document.getId());
                    productManagerFromDatabase.add(product);
                    Log.e("data","..."+ product);
                }
                RecyclerView productRecView = findViewById(R.id.ProductManageRecycleView);
                ProductManagerAdapter productManagerAdapter = ProductManagerAdapter.getInstance();
                productManagerAdapter.setProducts(productManagerFromDatabase);
                productManagerAdapter.setContext(this);
                productRecView.setAdapter(productManagerAdapter);
                productRecView.setLayoutManager(new LinearLayoutManager(this));
            } else {
                Log.e("Query data", "Error getting documents: ", task.getException());
            }
        });

        addProduct.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), AddProductActivity.class));
        });
        btnSignOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),SignIn.class));
            finish();
        });
    }
}