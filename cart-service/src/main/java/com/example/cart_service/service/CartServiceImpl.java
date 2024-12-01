package com.example.cart_service.service;

import com.example.cart_service.dto.CartDTO;
import com.example.cart_service.dto.CartItemDTO;
import com.example.cart_service.model.Cart;
import com.example.cart_service.model.CartItem;
import com.example.cart_service.repository.CartRepository;
import com.example.cart_service.exception.CartNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class  CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper modelMapper;
    private static final Logger logger = LogManager.getLogger(CartServiceImpl.class);

    @Override
    public CartDTO getCartByUserId (Long userId){
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));


        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setUserId(cart.getUserId());


        List<CartItemDTO> items = cart.getCartItem().stream().map(cartItem -> {
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


        cart.setCartItem(cartItems);
        Cart savedCart = cartRepository.save(cart);


        CartDTO responseDTO = new CartDTO();
        responseDTO.setId(savedCart.getId());
        responseDTO.setUserId(savedCart.getUserId());

        responseDTO.setItems(savedCart.getCartItem().stream().map(cartItem -> {
            CartItemDTO itemDTO = new CartItemDTO();
            itemDTO.setProductId(cartItem.getProductId());
            itemDTO.setQuantity(cartItem.getQuantity());
            itemDTO.setPrice(cartItem.getPrice());
            return itemDTO;
        }).collect(Collectors.toList()));

        logger.info("Cart created successfully with id: {}", savedCart.getId());
        return responseDTO;
    }

    @Override
    public CartDTO updateCart(Long userId, List<CartItemDTO> newItems) {
        logger.info("Updating cart for userId: {}", userId);

        Cart existingCart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));

        Map<Long, CartItem> existingCartItemMap = existingCart.getCartItem().stream()
                .collect(Collectors.toMap(CartItem::getProductId, item -> item));

        for (CartItemDTO newItem : newItems) {
            CartItem cartItem = existingCartItemMap.get(newItem.getProductId());
            if (cartItem != null) {
                cartItem.setQuantity(cartItem.getQuantity() + newItem.getQuantity());
            } else {
                CartItem newCartItem = new CartItem();
                newCartItem.setProductId(newItem.getProductId());
                newCartItem.setQuantity(newItem.getQuantity());
                newCartItem.setPrice(newItem.getPrice());
                newCartItem.setCart(existingCart);
                existingCart.getCartItem().add(newCartItem);
            }
        }

        Cart updatedCart = cartRepository.save(existingCart);

        CartDTO responseDTO = new CartDTO();
        responseDTO.setId(updatedCart.getId());
        responseDTO.setUserId(updatedCart.getUserId());
        responseDTO.setItems(updatedCart.getCartItem().stream().map(item -> {
            CartItemDTO itemDTO = new CartItemDTO();
            itemDTO.setProductId(item.getProductId());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setPrice(item.getPrice());
            return itemDTO;
        }).collect(Collectors.toList()));

        logger.info("Cart updated successfully for userId: {}", userId);
        return responseDTO;
    }




}
