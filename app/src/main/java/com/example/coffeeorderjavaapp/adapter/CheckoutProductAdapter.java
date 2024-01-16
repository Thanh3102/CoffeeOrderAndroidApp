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


public class CheckoutProductAdapter extends RecyclerView.Adapter<CheckoutProductAdapter.ViewHolder> {
    private final Context context;
    private final  FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<CartItem> cartItems;
    private int total = 0;

    public CheckoutProductAdapter(Context context, ArrayList<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checkout_product, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);

        db.collection("products").document(cartItem.getProduct().getId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Product product = task.getResult().toObject(Product.class);
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
            } else {
                Log.e("Error", "Error: ", task.getException());
            }
        });
    }


    @Override
    public int getItemCount() {
        return cartItems.size();
    }
    private int getToppingSizeTotal(CartItem cartItem) {
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
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView productNameTv;
        private final TextView productOptionTv;
        private final TextView productTotalTv;
        private final TextView productQuantityTv;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemCheckoutImageView);
            productNameTv = itemView.findViewById(R.id.itemCheckoutNameTv);
            productOptionTv = itemView.findViewById(R.id.itemCheckoutOptionTv);
            productTotalTv = itemView.findViewById(R.id.itemCheckoutTotalTv);
            productQuantityTv = itemView.findViewById(R.id.itemCheckoutQuantityTv);
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

    }

}

