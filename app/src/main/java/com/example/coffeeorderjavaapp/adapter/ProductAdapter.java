package com.example.coffeeorderjavaapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeorderjavaapp.ProductDetailActivity;
import com.example.coffeeorderjavaapp.R;
import com.example.coffeeorderjavaapp.model.Product;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private final List<Product> products;
    private final Context context;

    public ProductAdapter(List<Product> products, Context context){
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.getTvName().setText(product.getName());
        holder.getTvCategory().setText(product.getCategory());
        holder.getTvPrice().setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(product.getPrice()));
        Picasso.with(context).load(product.getImageURL()).into(holder.getIvProductImg());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ProductDetailActivity.class);
            intent.putExtra("productId", product.getId());
            holder.itemView.getContext().startActivity(intent);
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

        private final ImageView ivProductImg;

        public ViewHolder(View view) {
            super(view);
            this.tvName = (TextView) view.findViewById(R.id.productItemName);
            this.tvCategory = (TextView) view.findViewById(R.id.productItemCategory);
            this.tvPrice = (TextView) view.findViewById(R.id.productItemPrice);
            this.ivProductImg = (ImageView) view.findViewById(R.id.itemProductImageView);
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

        public ImageView getIvProductImg() {
            return ivProductImg;
        }
    }
}
