package com.example.coffeeorderjavaapp.model;

public class SizeOption {
    private String size_name;
    private int size_price;

    public SizeOption(){} // Dùng cho firebase

    public SizeOption(String size_name, int size_price) {
        this.size_name = size_name;
        this.size_price = size_price;
    }

    public String getSize_name() {
        return size_name;
    }

    @Override
    public String toString() {
        return "FirebaseSizeOption{" +
                "size_name='" + size_name + '\'' +
                ", size_price=" + size_price +
                '}';
    }

    public void setSize_name(String size_name) {
        this.size_name = size_name;
    }

    public int getSize_price() {
        return size_price;
    }

    public void setSize_price(int size_price) {
        this.size_price = size_price;
    }
}
