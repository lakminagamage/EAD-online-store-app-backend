package com.ead.cart_service.controller;

import com.ead.cart_service.dto.CartDTO;
import com.ead.cart_service.dto.CartItemDTO;
import com.ead.cart_service.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController extends AbstractController{

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<CartDTO> createCart(@RequestBody CartDTO cartDTO) {
        CartDTO createdCart = cartService.createCart(cartDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCart);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CartDTO> getCartByUserId(@PathVariable Long userId) {
        CartDTO cartDTO = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cartDTO);
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<CartDTO> addItemsToCart(@RequestBody CartItemDTO cartDTO, @PathVariable Long userId) {
        CartDTO updatedCart = cartService.addItemsToCart(cartDTO, userId);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/items/{cartItemId}/user/{userId}")
    public ResponseEntity<Void> deleteCartItems(@PathVariable Long cartItemId, @PathVariable Long userId) {
        cartService.deleteCartItems(cartItemId, userId);
        return ResponseEntity.noContent().build();
    }
}
