package com.example.coffeeorderjavaapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CheckoutActivity extends AppCompatActivity {

    private TextView deliveryTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        deliveryTv = findViewById(R.id.deliveryLocationTv);
        Button openMapBtn = findViewById(R.id.openMapBtn);
        Button backBtn = findViewById(R.id.checkoutBackBtn);

        openMapBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapActivity.class);
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
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String deliveryLocation = extras.getString("deliveryLocation");
            this.deliveryTv.setText("Tới: " + deliveryLocation);
        }
    }
}