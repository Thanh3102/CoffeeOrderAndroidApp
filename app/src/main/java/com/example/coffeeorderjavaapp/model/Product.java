package com.example.coffeeorderjavaapp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Product{
    private String id;
    private String name;
    private String description;
    private String category;
    private String imageURL;
    private int price;
    private ArrayList<SizeOption> size_options;

    private ArrayList<ToppingOption> topping_options;

    public Product(){}
    public Product(String name, String description, String category, String imageURL, int price, ArrayList<SizeOption> size_options, ArrayList<ToppingOption> topping_options) {

        this.name = name;
        this.description = description;
        this.category = category;
        this.imageURL = imageURL;
        this.price = price;
        this.size_options = size_options;
        this.topping_options = topping_options;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ArrayList<SizeOption> getSize_options() {
        return size_options;
    }

    public void setSize_options(ArrayList<SizeOption> size_options) {
        this.size_options = size_options;
    }

    @Override
    public String toString() {
        return "FirebaseProduct{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", price=" + price +
                ", size_options=" + size_options +
                ", topping_options=" + topping_options +
                '}';
    }

    public ArrayList<ToppingOption> getTopping_options() {
        return topping_options;
    }

    public void setTopping_options(ArrayList<ToppingOption> topping_options) {
        this.topping_options = topping_options;
    }
}
