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
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeorderjavaapp.R;
import com.example.coffeeorderjavaapp.model.CartItem;
import com.example.coffeeorderjavaapp.model.Product;
import com.example.coffeeorderjavaapp.model.ToppingOption;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private final Context context;
    private final TextView totalTv;
    private ArrayList<CartItem> cartItems;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int total = 0;

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
        if (position == 0) {
            this.total = 0;
        }
        CartItem cartItem = cartItems.get(position);
        Product product = cartItem.getProduct();
        ArrayList<String> toppingNameList = new ArrayList<>();

        for (ToppingOption option : cartItem.getTopping_options()) {
            toppingNameList.add(option.getTopping_name());
        }
        String toppingString = String.join(",", toppingNameList);

        String size = "";
        if (cartItem.getSize_option() != null) {
            size = "Size " + cartItem.getSize_option().getSize_name() + ", ";
        }

        int totalPrice = (product.getPrice() + getToppingSizeTotal(cartItem)) * cartItem.getQuantity();
        this.total += totalPrice;
        Picasso.with(context).load(product.getImageURL()).into(holder.getImageView());
        holder.getProductNameTv().setText(product.getName());
        holder.getProductOptionTv().setText(size + toppingString);
        holder.getProductTotalTv().setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(totalPrice));
        holder.getProductQuantityTv().setText("SL: " + cartItem.getQuantity());
        holder.getDeleteBtn().setOnClickListener(v -> {
            db.collection("carts").document(cartItem.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        int decreaseValue = (product.getPrice() + getToppingSizeTotal(cartItem)) * cartItem.getQuantity();
                        int newValue = this.total - decreaseValue;
                        this.total = newValue;
                        this.totalTv.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(newValue));
                        this.cartItems.remove(cartItem);
                        this.notifyItemRemoved(position);
                    })
                    .addOnFailureListener(e -> Log.w("CART DELETE ERROR", "Error deleting document", e));
        });

        if (position == cartItems.size() - 1) {
            this.totalTv.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(this.total));
        }
    }

        private int getToppingSizeTotal (CartItem cartItem){
            int sizePrice = 0;
            int toppingPrice = 0;
            if (cartItem.getTopping_options() != null) {
                for (ToppingOption toppingOption : cartItem.getTopping_options()) {
                    toppingPrice += toppingOption.getTopping_price();
                }
            }

            if (cartItem.getSize_option() != null) {
                sizePrice = cartItem.getSize_option().getSize_price();
            }

            return sizePrice + toppingPrice;

        }

        @Override
        public int getItemCount () {
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
        public void changeList (ArrayList < CartItem > cartItems) {
            this.cartItems = cartItems;
            notifyItemRangeRemoved(0, this.cartItems.size());
        }

        @SuppressLint("NotifyDataSetChanged")
        public void clearList () {
            this.cartItems.clear();
            this.totalTv.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(0));
            this.notifyDataSetChanged();
        }
    }
