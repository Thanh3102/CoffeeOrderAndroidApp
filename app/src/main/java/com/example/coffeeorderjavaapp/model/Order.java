package com.example.coffeeorderjavaapp.model;

import java.time.LocalDateTime;
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


    public Order(){}
    public Order(String delivery_location, ArrayList<OrderProduct> order_products, String payment_method, Date create_at, String user_id) {
        this.delivery_location = delivery_location;
        this.order_products = order_products;
        this.payment_method = payment_method;
        this.create_at = create_at;
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDelivery_location() {
        return delivery_location;
    }

    public void setDelivery_location(String delivery_location) {
        this.delivery_location = delivery_location;
    }

    public ArrayList<OrderProduct> getOrder_products() {
        return order_products;
    }

    public void setOrder_products(ArrayList<OrderProduct> order_products) {
        this.order_products = order_products;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Date create_at) {
        this.create_at = create_at;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
