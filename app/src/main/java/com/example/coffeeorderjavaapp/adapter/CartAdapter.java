package com.example.coffeeorderjavaapp.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeorderjavaapp.R;
import com.example.coffeeorderjavaapp.model.CartItem;
import com.example.coffeeorderjavaapp.model.Product;
import com.example.coffeeorderjavaapp.model.ToppingOption;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private final Context context;

    private final TextView totalTv;
    private ArrayList<CartItem> cartItems;

    public CartAdapter(Context context, TextView totalTv, ArrayList<CartItem> cartItems) {
        this.context = context;
        this.totalTv = totalTv;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products").document(cartItem.getProduct_id()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Product product = task.getResult().toObject(Product.class);
                ArrayList<String> toppingNameList = new ArrayList<>();
                for (ToppingOption option : cartItem.getTopping_options()) {
                    toppingNameList.add(option.getTopping_name());
                }
                Picasso.with(context).load(product.getImageURL()).into(holder.getImageView());
                String size = "";
                String toppingString = String.join(",", toppingNameList);
                if(cartItem.getSize_option() != null) {
                    size = "Size " + cartItem.getSize_option().getSize_name() + ", ";
                }
                holder.getProductNameTv().setText(product.getName());
                holder.getProductOptionTv().setText(size + toppingString);
                holder.getProductTotalTv().setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(cartItem.getTotal_price()));
                holder.getProductQuantityTv().setText("SL: " + cartItem.getQuantity());
                holder.getDeleteBtn().setOnClickListener(v -> {
                    db.collection("carts").document(cartItem.getId())
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                this.cartItems.remove(cartItem);
                                this.notifyItemRemoved(position);
                                Toast.makeText(context, "Delete successfully", Toast.LENGTH_SHORT).show();
                                this.totalTv.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(this.getTotalPrice()));
                            })
                            .addOnFailureListener(e -> Log.w("CART DELETE ERROR", "Error deleting document", e));
                });

                Log.e("TAG", cartItem.toString() );

            } else {
                Log.e("Error", "Error: ", task.getException());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView productNameTv;
        private final TextView productOptionTv;
        private final TextView productTotalTv;
        private final TextView productQuantityTv;
        private final TextView deleteBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemCartImageView);
            productNameTv = itemView.findViewById(R.id.itemCartNameTv);
            productOptionTv = itemView.findViewById(R.id.itemCartOptionTv);
            productTotalTv = itemView.findViewById(R.id.itemCartTotalTv);
            productQuantityTv = itemView.findViewById(R.id.itemCartQuantityTv);
            deleteBtn = itemView.findViewById(R.id.cartItemDeleteBtn);
        }

        public ImageView getImageView() {
            return imageView;
        }

        public TextView getProductNameTv() {
            return productNameTv;
        }

        public TextView getProductOptionTv() {
            return productOptionTv;
        }

        public TextView getProductTotalTv() {
            return productTotalTv;
        }

        public TextView getProductQuantityTv() {
            return productQuantityTv;
        }

        public TextView getDeleteBtn() {
            return deleteBtn;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void changeList(ArrayList<CartItem> cartItems){
        this.cartItems = cartItems;
        notifyItemRangeRemoved(0, this.cartItems.size());
    }
    @SuppressLint("NotifyDataSetChanged")
    public void clearList(){
        this.cartItems.clear();
        this.notifyDataSetChanged();
    }

    public int getTotalPrice(){
        int total = 0;
        for(CartItem item : this.cartItems){
            total += item.getTotal_price();
        }
        return total;
    }
}
