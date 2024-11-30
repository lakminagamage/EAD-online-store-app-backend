package com.ead.cart_service.model;


import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="cart_id")
    private Cart cart;
    private Long productId;
    private int quantity;
    private double price;
    public void setId(Long id) {
        this.id = id;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setPrice(double price) {
        this.price = price;
    }


}




