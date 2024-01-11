package com.example.coffeeorderjavaapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CheckoutActivity extends AppCompatActivity {
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        
        Button openMapBtn = findViewById(R.id.openMapBtn);
        Button backBtn = findViewById(R.id.checkoutBackBtn);

        openMapBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this,MapActivity.class);
            startActivity(intent);
        });

        backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "Resume Checkout", Toast.LENGTH_SHORT).show();
    }
}