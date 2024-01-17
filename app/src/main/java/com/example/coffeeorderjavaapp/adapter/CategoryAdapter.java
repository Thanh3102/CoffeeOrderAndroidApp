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

public class CategoryAdapter extends  RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private final List<String> categories = Arrays.asList("All", "Cà Phê", "Trà", "Cloud", "Hi-Tea Healthy", "Trà Xanh", "Đá xay", "Bánh&Snack");
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
            ProductAdapter.getInstance().filter(categoryName);
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
