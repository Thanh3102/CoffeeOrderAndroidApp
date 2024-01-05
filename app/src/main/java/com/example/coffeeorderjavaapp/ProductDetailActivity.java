package com.example.coffeeorderjavaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeorderjavaapp.model.Product;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Product product = (Product) getIntent().getSerializableExtra("product");
        this.bindData(product);
    }

    private void bindData(Product product){
        if(product == null){
            Intent intent = new Intent(this, MainActivity.class);
            Toast.makeText(this, "Can't load data", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }

        TextView nameTv = findViewById(R.id.detailProductName);
        TextView categoryTv = findViewById(R.id.detailProductCategory);
        TextView descTv = findViewById(R.id.detailProductDesc);
        TextView priceTv = findViewById(R.id.productDetailPrice);

        nameTv.setText(product.getName());
        categoryTv.setText(product.getCategory());
        descTv.setText(product.getDescription());
        priceTv.setText(String.valueOf(product.getPrice()));
    }
}