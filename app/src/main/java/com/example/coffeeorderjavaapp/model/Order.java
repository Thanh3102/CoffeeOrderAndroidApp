package com.example.coffeeorderjavaapp.model;

import java.util.ArrayList;
import java.util.Date;

public class Order {
    private String id;
    private String delivery_location;
    private ArrayList<OrderProduct> order_products;
    private String payment_method;
    private String status = "Đang chờ";
    private Date create_at;
    private String user_id;

}
