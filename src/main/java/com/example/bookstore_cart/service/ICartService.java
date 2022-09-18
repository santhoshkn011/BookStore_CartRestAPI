package com.example.bookstore_cart.service;

import com.example.bookstore_cart.dto.CartDTO;
import com.example.bookstore_cart.model.Cart;

import java.util.List;

public interface ICartService {
    Cart addCartData(CartDTO cartDTO);

    List<Cart> allCartList();

    Cart getCartDetailsByCartId(Long cartId);

    List<Cart> getCartDetailsByUserId(Long userId);

    List<Cart> getCartDetailsByToken(String token);

    String editCartByCartId(Long cartId, CartDTO cartDTO);

    String deleteCartByCartId(Long userId, Long cartId);
}
