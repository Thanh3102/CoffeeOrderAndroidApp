package com.example.coffeeorderjavaapp.model;

public class ToppingOption {
    private String topping_name;
    private int topping_price;

    public ToppingOption(){}

    @Override
    public String toString() {
        return "FirebaseToppingOption{" +
                "topping_name='" + topping_name + '\'' +
                ", topping_price=" + topping_price +
                '}';
    }

    public ToppingOption(String topping_name, int topping_price) {
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
