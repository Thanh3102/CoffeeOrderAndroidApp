package com.example.coffeeorderjavaapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeorderjavaapp.R;
import com.example.coffeeorderjavaapp.model.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductManagerAdapter extends RecyclerView.Adapter<ProductManagerAdapter.ViewHolder> {
    private static final ProductManagerAdapter productManagerAdapter = new ProductManagerAdapter();
    private ArrayList<Product> products;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Context context;
    private ProductManagerAdapter(){

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_manager, parent, false);
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
        holder.getDeleteBtn().setOnClickListener(v -> {
            db.collection("products").document(product.getId())
                    .delete()
                    .addOnSuccessListener(command -> {
                        this.products.remove(product);
                        notifyItemRemoved(position);
                    }).addOnFailureListener(e -> Log.w("Product DELETE ERROR", "Error deleting document", e));
        });
           }




    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvName;
        private final TextView tvCategory;
        private final TextView tvPrice;
        private final ImageView ivProductImg;
        private final TextView deleteBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvName = (TextView) itemView.findViewById(R.id.itemProductManagerNameTv);
            this.tvCategory = (TextView) itemView.findViewById(R.id.productItemCategory);
            this.tvPrice = (TextView) itemView.findViewById(R.id.itemProductManagerTotalTv);
            this.ivProductImg = (ImageView) itemView.findViewById(R.id.itemProductManagerImageView);
            this.deleteBtn = (TextView) itemView.findViewById(R.id.ProductManagerItemDeleteBtn);

        }

        public TextView getDeleteBtn() {
            return deleteBtn;
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

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static ProductManagerAdapter getInstance(){
        return productManagerAdapter;
    }

    public void addProduct(Product product){
        this.products.add(product);
        this.notifyItemInserted(products.size() - 1);
    }
}
