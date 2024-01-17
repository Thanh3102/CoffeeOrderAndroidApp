package com.example.coffeeorderjavaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Button btnSignOut = findViewById(R.id.btnSignOut);
        Button addProduct = findViewById(R.id.btnManageProducts);
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