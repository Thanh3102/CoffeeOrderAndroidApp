package com.example.coffeeorderjavaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.coffeeorderjavaapp.model.FirebaseProduct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Button testBtn = findViewById(R.id.testBtn);
        ArrayList<FirebaseProduct> products = new ArrayList<>();
        testBtn.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("products").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("Query data", document.getId() + " => " + document.getData());
                        FirebaseProduct product = document.toObject(FirebaseProduct.class);
                        product.setId(document.getId());
                        products.add(product);
                    }
                    Log.e("Log product list", "List product: " + products);
                } else {
                    Log.d("Query data", "Error getting documents: ", task.getException());
                }
            });


        });
    }
}




//            FirebaseProduct newProduct = new FirebaseProduct("Test", "Test", new ArrayList<String>(Arrays.asList("xyz", "abc")));
//            productCollectionRef.add(newProduct).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//                            Log.d("Add", "DocumentSnapshot written with ID: " + documentReference.getId());
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.w("Add", "Error adding document", e);
//                        }
//                    });
//        });
