package com.example.coffeeorderjavaapp.model;

import java.util.ArrayList;

public class CartItem {
    private String id;
    private String product_id;
    private String user_id;
    private int quantity;
    private SizeOption size_option;
    private ArrayList<ToppingOption> topping_options;

    public CartItem(){

    }
    public CartItem(String product_id, String user_id, int quantity, SizeOption size_option, ArrayList<ToppingOption> topping_options, int total_price) {
        this.product_id = product_id;
        this.user_id = user_id;
        this.quantity = quantity;
        this.size_option = size_option;
        this.topping_options = topping_options;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public SizeOption getSize_option() {
        return size_option;
    }

    public void setSize_option(SizeOption size_option) {
        this.size_option = size_option;
    }

    public ArrayList<ToppingOption> getTopping_options() {
        return topping_options;
    }

    public void setTopping_options(ArrayList<ToppingOption> topping_options) {
        this.topping_options = topping_options;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id='" + id + '\'' +
                ", product_id='" + product_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", quantity=" + quantity +
                ", size_option=" + size_option +
                ", topping_options=" + topping_options +
                '}';
    }
}
