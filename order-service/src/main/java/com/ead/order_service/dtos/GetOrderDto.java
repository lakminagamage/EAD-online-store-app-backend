package com.ead.order_service.dtos;

import java.sql.Date;

public class GetOrderDto {
    private Long id;
    private String user_id;
    private  Date  timestamp;
    private String payment_status;   

    //getters and setters
    public Long getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getPayment_status() {
        return payment_status;
    }
    
}
