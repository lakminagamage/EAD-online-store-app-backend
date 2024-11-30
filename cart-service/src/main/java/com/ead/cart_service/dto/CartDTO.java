package com.ead.cart_service.dto;

import com.ead.cart_service.model.CartItem;
import lombok.Data;

import java.util.List;


@Data
public class CartDTO {
    private Long id;
    private Long userId;
    private List<CartItemDTO> items;

}




