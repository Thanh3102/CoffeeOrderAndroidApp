package com.example.coffeeorderjavaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeorderjavaapp.R;
import com.example.coffeeorderjavaapp.model.Order;
import com.example.coffeeorderjavaapp.model.OrderProduct;
import com.example.coffeeorderjavaapp.model.ToppingOption;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Order> orderArrayList;

    public OrderAdapter(Context context, ArrayList<Order> orderArrayList) {
        this.context = context;
        this.orderArrayList = orderArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderArrayList.get(position);
        holder.idOrder.setText(order.getId());
        holder.status.setText(order.getStatus());
        holder.createDate.setText(order.getCreate_at().toString());
        holder.deliveryLocation.setText(order.getDelivery_location());
        holder.orderPriceTotal.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(countTotal(order)));
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.recyclerViewOrderProduct.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        OrderProductAdapter orderProductAdapter = new OrderProductAdapter(order.getOrder_products());
        holder.recyclerViewOrderProduct.setAdapter(orderProductAdapter);
        holder.recyclerViewOrderProduct.setLayoutManager(layoutManager);
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView idOrder;
        private final TextView createDate;
        private final TextView deliveryLocation;
        private final TextView orderPriceTotal;
        private final TextView status;
        private final RecyclerView recyclerViewOrderProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idOrder = itemView.findViewById(R.id.idOrder);
            createDate = itemView.findViewById(R.id.CreateDate);
            deliveryLocation = itemView.findViewById(R.id.deliverLocation);
            orderPriceTotal = itemView.findViewById(R.id.orderPriceTotal);
            status = itemView.findViewById(R.id.status);
            recyclerViewOrderProduct = itemView.findViewById(R.id.recyclerViewOrderProduct);
        }
    }

    private int countTotal(Order order) {
        int total = 0;

        for (OrderProduct orderProduct : order.getOrder_products()) {
            int productPrice = orderProduct.getProduct().getPrice();
            int sizePrice = 0;
            int toppingPrice = 0;

            if (orderProduct.getSize_option() != null) {
                sizePrice = orderProduct.getSize_option().getSize_price();
            }

            if (orderProduct.getTopping_options() != null) {
                ArrayList<ToppingOption> toppingOptions = orderProduct.getTopping_options();
                for (ToppingOption toppingOption : toppingOptions) {
                    toppingPrice += toppingOption.getTopping_price();
                }
            }

            total += (productPrice + sizePrice + toppingPrice) * orderProduct.getQuantity();
        }

        return total;
    }
}
