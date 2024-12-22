package com.example.cart_service.exception;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(Long id) {

        super("Cart not found with id: " + id);
    }
}