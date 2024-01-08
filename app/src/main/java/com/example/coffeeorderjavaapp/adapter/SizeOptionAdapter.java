package com.example.coffeeorderjavaapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeorderjavaapp.ProductDetailAdapterCallBack;
import com.example.coffeeorderjavaapp.R;
import com.example.coffeeorderjavaapp.model.SizeOption;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SizeOptionAdapter extends RecyclerView.Adapter<SizeOptionAdapter.ViewHolder>{
    private ProductDetailAdapterCallBack adapterCallback;
    private final ArrayList<SizeOption> options;

    public SizeOptionAdapter(Context context, ArrayList<SizeOption> options) {
        adapterCallback = ((ProductDetailAdapterCallBack) context);
        this.options = options;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_size_option, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SizeOption option = options.get(position);
        String optionPrice = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(option.getSize_price());
        holder.getTv().setText(option.getSize_name() + " + " + optionPrice);
        holder.itemView.setOnClickListener(v -> {
            adapterCallback.chooseSize(option);
        });
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView tv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv = itemView.findViewById(R.id.itemSizeOptionTv);
        }

        public TextView getTv() {
            return tv;
        }
    }
}
