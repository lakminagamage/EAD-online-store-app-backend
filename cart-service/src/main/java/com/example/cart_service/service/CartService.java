package com.ead.cart_service.service;
import com.ead.cart_service.dto.CartDTO;

public interface CartService {
    CartDTO createCart(CartDTO cartDTO);
    CartDTO getCartByUserId(Long userId);
//    CartDTO addItemsToCart(CartItemDTO cartItemDTO, Long userId);

}
