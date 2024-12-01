package com.example.cart_service.controller;

import com.example.cart_service.dto.CartDTO;
import com.example.cart_service.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
public class CartController extends AbstractController {
    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<CartDTO> createCart(@RequestBody CartDTO cartDTO) {
        if (cartDTO.getItems() == null || cartDTO.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        CartDTO createdCart = cartService.createCart(cartDTO);
        return createdResponse(createdCart);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<CartDTO> getCartByUserId(@PathVariable Long userId) {
        CartDTO cart = cartService.getCartByUserId(userId);
        return successResponse(cart, HttpStatus.OK);
    }

//    @PutMapping("/user/{userId}")
//    public ResponseEntity<CartDTO> addItemsToCart(@RequestBody CartItemDTO cartDTO, @PathVariable Long userId) {
//        CartDTO updatedCart = cartService.addItemsToCart(cartDTO, userId);
//        return ResponseEntity.ok(updatedCart);
//    }

}
