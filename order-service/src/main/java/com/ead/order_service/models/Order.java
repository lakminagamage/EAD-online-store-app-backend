package com.ead.order_service.models;

import java.sql.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.List;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String user_id;
    private  Date  timestamp;
    private String payment_status;
    private List<OrderItem> orderItems;

    public Order( String user_id, Date timestamp, String payment_status, List<OrderItem> orderItems) {
        this.user_id = user_id;
        this.timestamp = timestamp;
        this.payment_status = payment_status;
    }

    public long getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }


    public Date getTimestamp() {
        return timestamp;
    }

    public String isPayment_status() {
        return payment_status;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setID(Long id) {
        this.id = id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }



}
