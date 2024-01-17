package com.example.coffeeorderjavaapp.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeorderjavaapp.R;

import java.util.Arrays;
import java.util.List;

// @drawable/rounded_corner_bg3_stroke3 ==> Active color
public class CategoryAdapter extends  RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private List<String> categories = Arrays.asList("All", "Cà phê", "Trà", "Cloud", "Hi-Tea Healthy", "Trà xanh", "Đá xay", "Bánh&Snack");
    private String Active = "All";
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String categoryName = categories.get(position);
        holder.categoryTv.setText(categoryName);
        holder.categoryTv.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), categoryName, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView categoryTv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTv = itemView.findViewById(R.id.itemCategory);
        }
    }
}
