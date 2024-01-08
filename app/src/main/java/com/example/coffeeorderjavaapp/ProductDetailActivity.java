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
import com.example.coffeeorderjavaapp.model.CartItem;
import com.example.coffeeorderjavaapp.model.Product;
import com.example.coffeeorderjavaapp.model.SizeOption;
import com.example.coffeeorderjavaapp.model.ToppingOption;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity implements ProductDetailAdapterCallBack {

    private Product product;
    private final ArrayList<ToppingOption> checkedOptions = new ArrayList<>();

    private SizeOption sizeOption = null;

    private boolean isRequireSize = false;

    private int quantity = 1;

    private int total = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String productId = getIntent().getStringExtra("productId");
        TextView quantityTv = findViewById(R.id.productDetailQuantityTv);
        TextView priceTv = findViewById(R.id.productDetailPrice);
        Button backBtn = findViewById(R.id.productDetailBackBtn);
        Button addBtn = findViewById(R.id.productDetailAddCartBtn);
        Button decreaseQtyBtn = findViewById(R.id.detailProductDecreaseQtyBtn);
        Button increaseQtyBtn = findViewById(R.id.detailProductIncreaseQtyBtn);

        backBtn.setOnClickListener(v -> {
            finish();
        });

        increaseQtyBtn.setOnClickListener(v -> {
            int current = Integer.parseInt(quantityTv.getText().toString());
            if (current != 99) {
                int newValue = current + 1;
                this.quantity = newValue;
                quantityTv.setText(String.valueOf(newValue));
                this.countTotalPrice();
            } else {
                Toast.makeText(this, "Số lượng đã đạt tối đa", Toast.LENGTH_SHORT).show();
            }
        });

        decreaseQtyBtn.setOnClickListener(v -> {
            int current = Integer.parseInt(quantityTv.getText().toString());
            if (current != 1) {
                int newValue = current - 1;
                this.quantity = newValue;
                quantityTv.setText(String.valueOf(newValue));
                this.countTotalPrice();
            } else {
                Toast.makeText(this, "Số lượng nhỏ nhất là 1", Toast.LENGTH_SHORT).show();
            }
        });


        if (productId == null) {
            Intent intent = new Intent(this, MainActivity.class);
            Toast.makeText(this, "No Product Id", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        }


        DocumentReference docRef = db.collection("products").document(productId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Product product = document.toObject(Product.class);
                    product.setId(document.getId());
                    this.product = product;
                    this.bindView();
                }
            } else {
                Log.e("Error", "fail with", task.getException());
            }
        });


        addBtn.setOnClickListener(v -> {
            if (this.sizeOption == null && this.isRequireSize) {
                Toast.makeText(this, "Bạn chưa chọn size", Toast.LENGTH_SHORT).show();
                return;
            }
            CartItem addingItem = new CartItem(this.product.getId(),"TEST USER", this.quantity, this.sizeOption, this.checkedOptions, this.total);
            db.collection("carts").add(addingItem).addOnSuccessListener(documentReference -> {
                Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "ID: " + documentReference.getId(), Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
            });
        });


    }

    private void bindView() {
        TextView nameTv = findViewById(R.id.detailProductName);
        TextView categoryTv = findViewById(R.id.detailProductCategory);
        TextView descTv = findViewById(R.id.detailProductDesc);
        TextView priceTv = findViewById(R.id.productDetailPrice);
        ImageView imageView = findViewById(R.id.detailProductImage);
        RecyclerView sizeRecyclerView = findViewById(R.id.detailProductSizeRV);
        RecyclerView toppingRecycleView = findViewById(R.id.detailProductToppingRV);
        if (product.getSize_options() != null) {
            this.isRequireSize = true;
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

    private void countTotalPrice() {
        TextView priceTv = findViewById(R.id.productDetailPrice);
        TextView quantityTv = findViewById(R.id.productDetailQuantityTv);
        int quantity = Integer.parseInt(quantityTv.getText().toString());
        int productPrice = this.product.getPrice();
        int sizePrice = this.sizeOption == null ? 0 : this.sizeOption.getSize_price();
        int toppingPrice = this.countTotalToppingPrice();
        int newTotal = (productPrice + sizePrice + toppingPrice) * quantity;
        priceTv.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(newTotal));
        this.total = newTotal;
    }

    private int countTotalToppingPrice() {
        int total = 0;
        for (ToppingOption option : this.checkedOptions) {
            total += option.getTopping_price();
        }
        return total;
    }

    @Override
    public void addToppingOption(ToppingOption option) {
        this.checkedOptions.add(option);
        this.countTotalPrice();
        Toast.makeText(this, this.checkedOptions.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void removeToppingOption(ToppingOption option) {
        this.checkedOptions.remove(option);
        this.countTotalPrice();
        Toast.makeText(this, this.checkedOptions.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void chooseSize(SizeOption option) {
        this.sizeOption = option;
        this.countTotalPrice();
        Toast.makeText(this, this.sizeOption.toString(), Toast.LENGTH_SHORT).show();
    }
}
