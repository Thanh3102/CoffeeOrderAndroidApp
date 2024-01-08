package com.example.coffeeorderjavaapp;

import com.example.coffeeorderjavaapp.model.SizeOption;
import com.example.coffeeorderjavaapp.model.ToppingOption;

public interface ProductDetailAdapterCallBack {
    void addToppingOption(ToppingOption option);
    void removeToppingOption(ToppingOption option);

    void chooseSize(SizeOption option);
}
