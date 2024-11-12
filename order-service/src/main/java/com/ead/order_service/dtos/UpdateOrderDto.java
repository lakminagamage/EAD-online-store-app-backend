package com.ead.order_service.dtos;

public class UpdateOrderDto {
    private String payment_status;
    
    //getters and setters

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }
}
