package com.example.coffeeorderjavaapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.coffeeorderjavaapp.model.Product;
import com.example.coffeeorderjavaapp.model.SizeOption;
import com.example.coffeeorderjavaapp.model.ToppingOption;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coffeeorderjavaapp.model.Product;
import com.example.coffeeorderjavaapp.model.SizeOption;
import com.example.coffeeorderjavaapp.model.ToppingOption;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {

    private EditText edtProductName, edtDescription, edtCategory, edtPrice;
    private ImageView imageView;
    private String selectedImageURL = ""; // Store the selected image URL
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;


    private Button btnAddProduct, btnAddSize, btnAddTopping, btnSelectImage;
    private LinearLayout layoutSizeOptions, layoutToppingOptions;

    private ArrayList<SizeOption> sizeOptions = new ArrayList<>();
    private ArrayList<ToppingOption> toppingOptions = new ArrayList<>();

    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private final ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult o) {
                            if (o.getResultCode() == RESULT_OK) {
                                if (o.getData() != null) {
                                    imageUri = o.getData().getData();
                                    btnAddProduct.setEnabled(true);
                                    Picasso.with(getApplicationContext()).load(imageUri).into(imageView);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Please select an image", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            // Load selected image using Picasso
            Picasso.with(getApplicationContext()).load(imageUri).into(imageView);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Initialize UI components
        edtProductName = findViewById(R.id.edtProductName);
        edtDescription = findViewById(R.id.edtDescription);
        edtCategory = findViewById(R.id.edtCategory);

        edtPrice = findViewById(R.id.edtPrice);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnAddSize = findViewById(R.id.btnAddSize);
        btnAddTopping = findViewById(R.id.btnAddTopping);
        layoutSizeOptions = findViewById(R.id.layoutSizeOptions);
        layoutToppingOptions = findViewById(R.id.layoutToppingOptions);
        imageView = findViewById(R.id.imageView);
        btnSelectImage = findViewById(R.id.btnSelectImage);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("product");


        // Set onClickListener for Add Product button
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });

        // Set onClickListener for Add Size button
        btnAddSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSizeOption();
            }
        });

        // Set onClickListener for Add Topping button
        btnAddTopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToppingOption();
            }
        });
        // Set onClickListener for Select Image button
        btnSelectImage.setOnClickListener(v -> selectImage());
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activityResultLauncher.launch(intent);
    }

    private void addSizeOption() {
        // Inflate SizeOption layout
        View sizeOptionView = LayoutInflater.from(this).inflate(R.layout.size_option_layout, null);

        // Add the SizeOption view to the layout
        layoutSizeOptions.addView(sizeOptionView);
    }

    private void addToppingOption() {
        // Inflate ToppingOption layout
        View toppingOptionView = LayoutInflater.from(this).inflate(R.layout.topping_option_layout, null);

        // Add the ToppingOption view to the layout
        layoutToppingOptions.addView(toppingOptionView);
    }

    private void addProduct() {
        // Get input values
        String productName = edtProductName.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String category = edtCategory.getText().toString().trim();

        int price = Integer.parseInt(edtPrice.getText().toString().trim());

        // Iterate over SizeOption views and add to the list
        for (int i = 0; i < layoutSizeOptions.getChildCount(); i++) {
            View sizeOptionView = layoutSizeOptions.getChildAt(i);
            EditText edtSizeName = sizeOptionView.findViewById(R.id.edtSizeName);
            EditText edtSizePrice = sizeOptionView.findViewById(R.id.edtSizePrice);

            String sizeName = edtSizeName.getText().toString().trim();
            int sizePrice = Integer.parseInt(edtSizePrice.getText().toString().trim());

            SizeOption sizeOption = new SizeOption(sizeName, sizePrice);
            sizeOptions.add(sizeOption);
        }

        // Iterate over ToppingOption views and add to the list
        for (int i = 0; i < layoutToppingOptions.getChildCount(); i++) {
            View toppingOptionView = layoutToppingOptions.getChildAt(i);
            EditText edtToppingName = toppingOptionView.findViewById(R.id.edtToppingName);
            EditText edtToppingPrice = toppingOptionView.findViewById(R.id.edtToppingPrice);

            String toppingName = edtToppingName.getText().toString().trim();
            int toppingPrice = Integer.parseInt(edtToppingPrice.getText().toString().trim());

            ToppingOption toppingOption = new ToppingOption(toppingName, toppingPrice);
            toppingOptions.add(toppingOption);
        }

        if (imageUri != null) {
            uploadImageToStorage(imageUri, productName, description, category, price);
        } else {

            // Create Product object
            Product product = new Product(productName, description, category, "", price, sizeOptions, toppingOptions);

        // Save product to Firestore
            saveProductToFirestore(product);
    }
    }
    private void uploadImageToStorage(Uri imageUri, String productName, String description, String category, int price) {
        // Tạo tên duy nhất cho ảnh, sử dụng UUID để tránh trùng lặp
        String imageName = UUID.randomUUID().toString();

        // Tham chiếu đến vị trí lưu trữ của ảnh trên Storage
        StorageReference imageRef = storageRef.child(imageName);

        // Đưa ảnh lên Firebase Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Lấy URL của ảnh sau khi tải lên thành công
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Cập nhật đường dẫn URL vào đối tượng Product
                        String imageUrl = uri.toString();
                        Product product = new Product(productName, description, category, imageUrl, price, sizeOptions, toppingOptions);

                        // Lưu thông tin sản phẩm vào Firestore
                        saveProductToFirestore(product);
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddProductActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveProductToFirestore(Product product) {
        // Lưu thông tin sản phẩm vào Firestore
        firestore.collection("products").add(product)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddProductActivity.this, "Failed to add product", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
