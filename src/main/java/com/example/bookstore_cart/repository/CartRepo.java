package com.example.bookstore_cart.repository;

import com.example.bookstore_cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
    @Query(value = "SELECT * FROM cart WHERE user_id=:userId", nativeQuery = true)
    List<Cart> getCartListWithUserId(Long userId);
    @Transactional
    @Modifying
    @Query(value = "delete from cart where cart_id = :cartId", nativeQuery = true)
    void deleteByCartId(Long cartId);
}
