package com.example.coffeeorderjavaapp.model;

import java.util.ArrayList;

public class OrderProduct {
    private Product product;
    private ArrayList<ToppingOption> topping_options;
    private SizeOption size_option;
    private int quantity;

    public OrderProduct(Product product, ArrayList<ToppingOption> topping_options, SizeOption size_option, int quantity) {
        this.product = product;
        this.topping_options = topping_options;
        this.size_option = size_option;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct_id(Product product) {
        this.product = product;
    }

    public ArrayList<ToppingOption> getTopping_options() {
        return topping_options;
    }

    public void setTopping_options(ArrayList<ToppingOption> topping_options) {
        this.topping_options = topping_options;
    }

    public SizeOption getSize_option() {
        return size_option;
    }

    public void setSize_option(SizeOption size_option) {
        this.size_option = size_option;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
