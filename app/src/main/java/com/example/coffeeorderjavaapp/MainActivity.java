package com.example.coffeeorderjavaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import com.example.coffeeorderjavaapp.adapter.MainViewPagerAdapter;
import com.example.coffeeorderjavaapp.adapter.ProductAdapter;
import com.example.coffeeorderjavaapp.model.Product;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                tabLayout.getTabAt(position).select();
            }
        });

//        List<Product> products = new ArrayList<Product>();
//        products.add(new Product(1, "Cà Phê Sữa Đá", "Cà phê", 29000));
//        products.add(new Product(2, "Caramel Muối Phin Sữa Tươi", "Cà phê", 69000));
//        products.add(new Product(3, "Frosty Caramel Muối Arabica", "Cà phê", 72000));
//        products.add(new Product(4, "Trà Xanh Espresso Marble", "Trà xanh", 49000));
//        products.add(new Product(5, "Cà Phê Sữa Nóng", "Cà phê", 39000));
//        products.add(new Product(6, "CloudTea Trà Xanh Tây Bắc", "Cloud", 69000));
//        products.add(new Product(7, "CloudTea Oolong Nướng Kem Dừa Đá Xay", "Cloud", 55000));
//        products.add(new Product(8, "Frosty Phin-Gato", "Đá xay", 55000));
//
//        RecyclerView productRecView = this.findViewById(R.id.productListRecView);
//        ProductAdapter productAdapter = new ProductAdapter(products);
//        productRecView.setAdapter(productAdapter);
    }
}