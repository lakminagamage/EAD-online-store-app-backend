package com.ead.order_service.dtos;


public class CreateOrderDto {
    private int user_id;
    private String product_id;
    private int quantity;
    private double price;

    //getters and setters
    public int getUser_id() {
        return user_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
