package com.example.coffeeorderjavaapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeorderjavaapp.R;
import com.example.coffeeorderjavaapp.adapter.CartAdapter;
import com.example.coffeeorderjavaapp.model.CartItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;

public class CartFragment extends Fragment {

    public CartFragment() {
        // Required empty public constructor
    }

    private final ArrayList<CartItem> cartItems = new ArrayList<>();
    private CartAdapter cartAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart, container, false);
        Log.e("CART FRAGMENT", "onCreateView: ");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("carts").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    CartItem cartItem = doc.toObject(CartItem.class);
                    cartItem.setId(doc.getId());
                    this.cartItems.add(cartItem);
                }
                RecyclerView cartRecV = root.findViewById(R.id.cartRecycleView);
                CartAdapter cartAdapter = new CartAdapter(this.getContext(), this.cartItems);
                cartRecV.setAdapter(cartAdapter);
                this.cartAdapter = cartAdapter;
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
                cartRecV.setLayoutManager(linearLayoutManager);
            }
        });

        TextView deleteAllTv = root.findViewById(R.id.cartDeleteAllBtn);
        deleteAllTv.setOnClickListener(v -> {
            db.collection("carts")
                    .whereEqualTo("user_id", "TEST USER")
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            WriteBatch batch = db.batch();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                batch.delete(db.collection("carts").document(document.getId()));
                            }
                            batch.commit().addOnSuccessListener(unused -> {
                                Toast.makeText(getContext(), "Delete all successfully", Toast.LENGTH_SHORT).show();
                                this.cartAdapter.clearList();
                            }).addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Delete all failed", Toast.LENGTH_SHORT).show();
                            });
                        } else {
                            Log.d("DELETE ALL ERROR", "Error getting documents: ", task.getException());
                        }
                    });
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("carts").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<CartItem> newList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    CartItem cartItem = doc.toObject(CartItem.class);
                    cartItem.setId(doc.getId());
                    newList.add(cartItem);
                }
                this.cartAdapter.changeList(newList);
            }
        });
    }
}