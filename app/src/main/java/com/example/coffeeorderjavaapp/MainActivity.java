package com.example.coffeeorderjavaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.coffeeorderjavaapp.adapter.MainViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(this, TestActivity.class);
//        startActivity(intent);


//        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//        DocumentReference productDocRef = firestore.collection("products").document("3Iz1NexPkZxlqwWGaCag");
//        productDocRef.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                DocumentSnapshot document = task.getResult();
//                if (document.exists()) {
//                    Log.d("Firebase", "DocumentSnapshot data: " + document.getData());
//                    FirebaseProduct x = document.toObject(FirebaseProduct.class);
//                    Log.e("Convert to object", x.toString());
//                } else {
//                    Log.d("Firebase", "No such document");
//                }
//            } else {
//                Log.d("Firebase", "get failed with ", task.getException());
//            }
//        });

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
            public void onPageSelected(int position){
                tabLayout.getTabAt(position).select();
            }
        });


    }
}