package com.ead.cart_service.service;

import com.ead.cart_service.dto.CartDTO;
import com.ead.cart_service.dto.CartItemDTO;
import com.ead.cart_service.model.Cart;
import com.ead.cart_service.model.CartItem;
import com.ead.cart_service.repository.CartRepository;
import com.ead.cart_service.exception.CartNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartRepository cartRepository;

    private static final Logger logger = LogManager.getLogger(CartServiceImpl.class);

    @Override
    public CartDTO getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserID(userId);

        return modelMapper.map(cart, CartDTO.class);
    }

    @Override
    public CartDTO createCart(CartDTO cartDTO) {
        Cart cart = modelMapper.map(cartDTO, Cart.class);
        cart = cartRepository.save(cart);
        return modelMapper.map(cart, CartDTO.class);
    }

    @Override
    public CartDTO addItemsToCart(CartItemDTO cartItemDTO, Long userId) {
        // find cart by id
        Cart cart = this.cartRepository.findByUserID(userId);



        return null;
    }

    @Override
    public CartDTO deleteCartItems(Long id, Long userId) {
        Cart cart = this.cartRepository.findByUserID(userId);



        cart = cartRepository.save(cart);
        return modelMapper.map(cart, CartDTO.class);
    }


}