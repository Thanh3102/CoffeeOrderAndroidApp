package com.example.coffeeorderjavaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.coffeeorderjavaapp.adapter.SearchProductAdapter;
import com.example.coffeeorderjavaapp.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView searchRV;
    private SearchProductAdapter searchProductAdapter;
    private ArrayList<Product> productArrayList;
    private SearchView searchView;
    private ImageView imageView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        db = FirebaseFirestore.getInstance();

        searchRV = findViewById(R.id.idRVCourses);
        searchView = findViewById(R.id.search_view);
        imageView = findViewById(R.id.backMain);

        buildRecyclerView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void filter(String text) {
        ArrayList<Product> filteredlist = new ArrayList<Product>();
        for (Product item : productArrayList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            searchProductAdapter.filterList(filteredlist);
        }
    }

    private void buildRecyclerView() {
        productArrayList = new ArrayList<Product>();
        db.collection("products").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Product c = d.toObject(Product.class);
                                c.setId(d.getId());
                                productArrayList.add(c);
                            }
                            searchProductAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(SearchActivity.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SearchActivity.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
                    }
                });

        searchProductAdapter = new SearchProductAdapter(productArrayList, SearchActivity.this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        searchRV.setHasFixedSize(true);
        searchRV.setLayoutManager(manager);
        searchRV.setAdapter(searchProductAdapter);
    }
}