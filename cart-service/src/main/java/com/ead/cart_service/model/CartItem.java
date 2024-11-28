package com.ead.cart_service.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name="cart_id")
    @ManyToOne
    private Cart cart;

    private Long productId;

    private int quantity;



}




