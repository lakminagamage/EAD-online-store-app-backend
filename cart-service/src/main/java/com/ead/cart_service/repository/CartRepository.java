package com.ead.cart_service.repository;

import com.ead.cart_service.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query ("SELECT c FROM Cart c WHERE c.userId = :userId")
    Cart findByUserID(Long userId);

}
