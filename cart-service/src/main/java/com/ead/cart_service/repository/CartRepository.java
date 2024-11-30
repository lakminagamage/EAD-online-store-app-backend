package com.ead.cart_service.repository;

import com.ead.cart_service.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;


public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query ("SELECT c FROM Cart c WHERE c.userId = :userId")
    Optional <Cart> findByUserID(Long userId);

}
