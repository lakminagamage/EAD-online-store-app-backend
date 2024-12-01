package com.example.cart_service.dto;
import lombok.Data;


@Data
public class CartItemDTO {
    private Long productId;
    private int quantity;
    private double price;
}
