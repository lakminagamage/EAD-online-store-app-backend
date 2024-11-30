package com.ead.cart_service.service;
import com.ead.cart_service.dto.CartDTO;
import com.ead.cart_service.dto.CartItemDTO;

import java.util.List;

public interface CartService {
    CartDTO createCart(CartDTO cartDTO);
    CartDTO getCartByUserId(Long userId);
//    CartDTO addItemsToCart(CartItemDTO cartItemDTO, Long userId);

}
