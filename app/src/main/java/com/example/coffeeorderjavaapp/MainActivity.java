package com.example.coffeeorderjavaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.coffeeorderjavaapp.adapter.MainViewPagerAdapter;
import com.example.coffeeorderjavaapp.adapter.ProductAdapter;
import com.example.coffeeorderjavaapp.model.FirebaseProduct;
import com.example.coffeeorderjavaapp.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);


        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference productDocRef = firestore.collection("products").document("3Iz1NexPkZxlqwWGaCag");
        productDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d("Firebase", "DocumentSnapshot data: " + document.getData());
                    FirebaseProduct x = document.toObject(FirebaseProduct.class);
                    Log.e("Convert to object", x.toString());
                } else {
                    Log.d("Firebase", "No such document");
                }
            } else {
                Log.d("Firebase", "get failed with ", task.getException());
            }
        });

        TabLayout tabLayout = findViewById(R.id.navigationBottomLayout);
        ViewPager2 viewPager = findViewById(R.id.mainViewPager);
        MainViewPagerAdapter vpAdapter = new MainViewPagerAdapter(this);
        viewPager.setAdapter(vpAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                Log.e("Position", "onPageScrolled: " + position );
                tabLayout.getTabAt(position).select();
            }
        });


    }
}