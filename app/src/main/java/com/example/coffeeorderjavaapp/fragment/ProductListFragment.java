package com.example.coffeeorderjavaapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeorderjavaapp.R;
import com.example.coffeeorderjavaapp.adapter.CategoryAdapter;
import com.example.coffeeorderjavaapp.adapter.ProductAdapter;
import com.example.coffeeorderjavaapp.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_list, container, false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CategoryAdapter categoryAdapter = new CategoryAdapter();
        RecyclerView categoryRecView = rootView.findViewById(R.id.categoryRecView);
        categoryRecView.setAdapter(categoryAdapter);


        ArrayList<Product> productFromDatabase = new ArrayList<>();
        db.collection("products").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot document : task.getResult()){
                    Product product = document.toObject(Product.class);
                    product.setId(document.getId());
                    Log.e("Convert product", product.toString());
                    productFromDatabase.add(product);
                }
                RecyclerView productRecView = rootView.findViewById(R.id.productListRecView);
                ProductAdapter productAdapter = new ProductAdapter(productFromDatabase, rootView.getContext());
                productRecView.setAdapter(productAdapter);
            }
            else {
                Log.e("Query data", "Error getting documents: ", task.getException());
            }
        });

        return rootView;
    }
}
