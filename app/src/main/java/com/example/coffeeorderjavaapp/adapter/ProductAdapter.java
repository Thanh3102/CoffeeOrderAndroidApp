package com.example.coffeeorderjavaapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeorderjavaapp.ProductDetailActivity;
import com.example.coffeeorderjavaapp.R;
import com.example.coffeeorderjavaapp.model.Product;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private static final ProductAdapter productAdapter = new ProductAdapter();
    private List<Product> products;
    private final List<Product> filterProducts = new ArrayList<>();
    private Context context;


    private ProductAdapter(){}

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
        Product product = filterProducts.get(position);
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
        return filterProducts.size();
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        filterProducts.addAll(products);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static ProductAdapter getInstance(){
        return productAdapter;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filter(String category){
        filterProducts.clear();
        if(category.equals("All")){
            filterProducts.addAll(products);
            this.notifyDataSetChanged();
            return;
        }
        for(Product product : products){
            if (product.getCategory().equals(category)){
                filterProducts.add(product);
            }
        }
        if (filterProducts.size() == 0){
            Toast.makeText(context, "Không có sản phẩm", Toast.LENGTH_SHORT).show();
        }
        this.notifyDataSetChanged();
    }
}
