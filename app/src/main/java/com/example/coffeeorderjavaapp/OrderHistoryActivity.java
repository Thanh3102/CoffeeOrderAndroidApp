package com.example.coffeeorderjavaapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeorderjavaapp.adapter.OrderAdapter;
import com.example.coffeeorderjavaapp.model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class OrderHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private ArrayList<Order> orderArrayList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        recyclerView = findViewById(R.id.idRecyclerViewOrder);
        buildRecyclerView();
    }

    private void buildRecyclerView() {
        orderArrayList = new ArrayList<>();
        db.collection("orders").whereEqualTo("user_id", user.getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Order c = doc.toObject(Order.class);
                            c.setId(doc.getId());
                            orderArrayList.add(c);
                        }
                        orderAdapter = new OrderAdapter(OrderHistoryActivity.this, orderArrayList);
                        LinearLayoutManager manager = new LinearLayoutManager(this);
                        recyclerView.setAdapter(orderAdapter);
                        recyclerView.setLayoutManager(manager);
                    } else {
                        Log.e("Get order status", "fail");
                    }
                }).addOnFailureListener(e -> Toast.makeText(OrderHistoryActivity.this, "Fail to get the data.", Toast.LENGTH_SHORT).show());
    }
}