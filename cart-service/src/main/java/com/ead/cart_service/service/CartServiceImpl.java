package com.ead.cart_service.service;

import com.ead.cart_service.dto.CartDTO;
import com.ead.cart_service.dto.CartItemDTO;
import com.ead.cart_service.model.Cart;
import com.ead.cart_service.model.CartItem;
import com.ead.cart_service.repository.CartRepository;
import com.ead.cart_service.exception.CartNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {


    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private static final Logger logger = LogManager.getLogger(CartServiceImpl.class);


    @Override
    public CartDTO getCartByUserId (Long userId){
        Cart cart = cartRepository.findByUserID(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));


        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setUserId(cart.getUserId());


        List<CartItemDTO> items = cart.getCartItems().stream().map(cartItem -> {
            CartItemDTO itemDTO = new CartItemDTO();
            itemDTO.setProductId(cartItem.getProductId());
            itemDTO.setQuantity(cartItem.getQuantity());
            itemDTO.setPrice(cartItem.getPrice());
            return itemDTO;
        }).collect(Collectors.toList());

        cartDTO.setItems(items);
        return cartDTO;
    }

    @Override
    public CartDTO createCart(CartDTO cartDTO) {
        logger.info("Creating cart for userId: {}", cartDTO.getUserId());

        Cart cart = new Cart();
        cart.setUserId(cartDTO.getUserId());

        List<CartItem> cartItems = cartDTO.getItems().stream().map(itemDTO -> {
            CartItem cartItem = new CartItem();
            cartItem.setProductId(itemDTO.getProductId());
            cartItem.setQuantity(itemDTO.getQuantity());
            cartItem.setPrice(itemDTO.getPrice());
            cartItem.setCart(cart);
            return cartItem;
        }).collect(Collectors.toList());

        cart.setCartItems(cartItems);
        Cart savedCart = cartRepository.save(cart);


        CartDTO responseDTO = new CartDTO();
        responseDTO.setId(savedCart.getId());
        responseDTO.setUserId(savedCart.getUserId());

        responseDTO.setItems(savedCart.getCartItems().stream().map(cartItem -> {
            CartItemDTO itemDTO = new CartItemDTO();
            itemDTO.setProductId(cartItem.getProductId());
            itemDTO.setQuantity(cartItem.getQuantity());
            itemDTO.setPrice(cartItem.getPrice());
            return itemDTO;
        }).collect(Collectors.toList()));

        logger.info("Cart created successfully with id: {}", savedCart.getId());
        return responseDTO;
    }



}