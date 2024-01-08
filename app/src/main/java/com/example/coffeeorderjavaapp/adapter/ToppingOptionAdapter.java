package com.example.coffeeorderjavaapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeorderjavaapp.R;
import com.example.coffeeorderjavaapp.ProductDetailAdapterCallBack;
import com.example.coffeeorderjavaapp.model.ToppingOption;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ToppingOptionAdapter extends RecyclerView.Adapter<ToppingOptionAdapter.Viewholder> {
    private ProductDetailAdapterCallBack adapterCallback;
    private ArrayList<ToppingOption> options;

    public ToppingOptionAdapter(Context context, ArrayList<ToppingOption> options) {
        adapterCallback = ((ProductDetailAdapterCallBack) context);
        this.options = options;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topping_option, parent, false);
        return new Viewholder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        ToppingOption option = options.get(position);
        String priceFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(option.getTopping_price());
        holder.getToppingTv().setText(option.getTopping_name() + " + " + priceFormat);
        holder.getToppingCheckBox().setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                adapterCallback.addToppingOption(option);
            } else {
                adapterCallback.removeToppingOption(option);
            }
        });
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        private final CheckBox toppingCheckBox;
        private final TextView toppingTv;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            toppingTv = itemView.findViewById(R.id.toppingTv);
            toppingCheckBox = itemView.findViewById(R.id.toppingCheckbox);
        }

        public CheckBox getToppingCheckBox() {
            return toppingCheckBox;
        }

        public TextView getToppingTv() {
            return toppingTv;
        }
    }

}
