package com.ead.cart_service.dto;

import com.ead.cart_service.model.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Data

public class CartDTO {
    private Long id;
    private Long userid;
    private List<CartItem> cartItem;

}




