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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_list, container, false);


        CategoryAdapter categoryAdapter = new CategoryAdapter();
        RecyclerView categoryRecView = rootView.findViewById(R.id.categoryRecView);
        categoryRecView.setAdapter(categoryAdapter);

        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Cà Phê Sữa Đá", "Cà phê", 29000));
        products.add(new Product(2, "Caramel Muối Phin Sữa Tươi", "Cà phê", 69000));
        products.add(new Product(3, "Frosty Caramel Muối Arabica", "Cà phê", 72000));
        products.add(new Product(4, "Trà Xanh Espresso Marble", "Trà xanh", 49000));
        products.add(new Product(5, "Cà Phê Sữa Nóng", "Cà phê", 39000));
        products.add(new Product(6, "CloudTea Trà Xanh Tây Bắc", "Cloud", 69000));
        products.add(new Product(7, "CloudTea Oolong Nướng Kem Dừa Đá Xay", "Cloud", 55000));
        products.add(new Product(8, "Frosty Phin-Gato", "Đá xay", 55000));

        RecyclerView productRecView = rootView.findViewById(R.id.productListRecView);
        ProductAdapter productAdapter = new ProductAdapter(products);
        productRecView.setAdapter(productAdapter);
        return rootView;
    }
}
