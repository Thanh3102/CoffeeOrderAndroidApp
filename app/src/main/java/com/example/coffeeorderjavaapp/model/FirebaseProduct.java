package com.example.coffeeorderjavaapp.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class FirebaseProduct {
    private String id = null;
    private String name;
    private String description;
    private ArrayList<String> favor_option = null;

    public FirebaseProduct(){
    }

    public FirebaseProduct(String name, String desc, ArrayList<String> favor_options) {
        this.name = name;
        this.description = desc;
        this.favor_option = favor_options;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public ArrayList<String> getFavor_option() {
        return favor_option;
    }

    public void setFavor_option(ArrayList<String> favor_option) {
        this.favor_option = favor_option;
    }

    @NonNull
    @Override
    public String toString() {
        return "FirebaseProduct{" +
                "id='" + id + "\'" +
                "name='" + name + '\'' +
                ", desc='" + description + '\'' +
                ", favor_options=" + favor_option +
                '}';
    }
}
