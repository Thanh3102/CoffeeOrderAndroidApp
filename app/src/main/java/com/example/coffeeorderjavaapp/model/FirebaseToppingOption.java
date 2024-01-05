package com.example.coffeeorderjavaapp.model;

public class FirebaseToppingOption {
    private String topping_name;
    private int topping_price;

    public FirebaseToppingOption(){}

    @Override
    public String toString() {
        return "FirebaseToppingOption{" +
                "topping_name='" + topping_name + '\'' +
                ", topping_price=" + topping_price +
                '}';
    }

    public FirebaseToppingOption(String topping_name, int topping_price) {
        this.topping_name = topping_name;
        this.topping_price = topping_price;
    }

    public String getTopping_name() {
        return topping_name;
    }

    public void setTopping_name(String topping_name) {
        this.topping_name = topping_name;
    }

    public int getTopping_price() {
        return topping_price;
    }

    public void setTopping_price(int topping_price) {
        this.topping_price = topping_price;
    }
}
