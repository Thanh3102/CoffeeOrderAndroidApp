package com.example.coffeeorderjavaapp.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeorderjavaapp.ProductDetailActivity;
import com.example.coffeeorderjavaapp.R;
import com.example.coffeeorderjavaapp.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private final List<Product> products;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.getTvName().setText(product.getName());
        holder.getTvCategory().setText(product.getCategory());
        holder.getTvPrice().setText(Double.toString(product.getPrice()) + "đ");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), ProductDetailActivity.class);
                intent.putExtra("PostId", product.getId());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvCategory;
        private final TextView tvPrice;

        public ViewHolder(View view) {
            super(view);
            this.tvName = (TextView) view.findViewById(R.id.productItemName);
            this.tvCategory = (TextView) view.findViewById(R.id.productItemCategory);
            this.tvPrice = (TextView) view.findViewById(R.id.productItemPrice);
        }

        public TextView getTvName() {
            return tvName;
        }

        public TextView getTvCategory() {
            return tvCategory;
        }

        public TextView getTvPrice() {
            return tvPrice;
        }
    }

    public ProductAdapter(List<Product> products) {
        this.products = products;
    }


}
