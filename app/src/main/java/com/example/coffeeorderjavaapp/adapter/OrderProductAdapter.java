package com.example.coffeeorderjavaapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeorderjavaapp.R;
import com.example.coffeeorderjavaapp.model.OrderProduct;
import com.example.coffeeorderjavaapp.model.ToppingOption;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.ViewHolder> {
    private ArrayList<OrderProduct> productArrayList;

    public OrderProductAdapter(ArrayList<OrderProduct> productArrayList) {
        this.productArrayList = productArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderProduct orderProduct = productArrayList.get(position);
        String sizeName = "Mặc định";
        String ToppingName = "Không";
        if(orderProduct.getSize_option() != null ){ sizeName = orderProduct.getSize_option().getSize_name();}
        if(orderProduct.getTopping_options().size() != 0){ ToppingName = getToppingNameString(orderProduct.getTopping_options());}
        holder.ProductName.setText(orderProduct.getProduct().getName());
        holder.ProductQuantity.setText(String.valueOf(orderProduct.getQuantity()));
        holder.sizeName.setText(sizeName);
        holder.toppingName.setText(ToppingName);
        holder.ProductPrice.setText("Tổng: " + NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(orderProduct.getProduct().getPrice()));
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView ProductName;
        private final TextView ProductQuantity;
        private final TextView sizeName;
        private final TextView toppingName;
        private final TextView ProductPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ProductName = itemView.findViewById(R.id.ProductName);
            ProductQuantity = itemView.findViewById(R.id.ProductQuantity);
            sizeName = itemView.findViewById(R.id.sizeName);
            toppingName = itemView.findViewById(R.id.toppingName);
            ProductPrice = itemView.findViewById(R.id.ProductPrice);
        }
    }

    private String getToppingNameString(ArrayList<ToppingOption> toppingOptions){
        ArrayList<String> toppingNameList = new ArrayList<>();
        for (ToppingOption option : toppingOptions) {
            toppingNameList.add(option.getTopping_name());
        }
        String toppingString = String.join(",", toppingNameList);
        return toppingString;
    }

}
