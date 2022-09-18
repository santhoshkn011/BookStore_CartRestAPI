package com.example.bookstore_cart.model;

import com.example.bookstore_cart.dto.CartDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cartId", nullable = false)
    Long cartId;
    Long userId;
    Long bookId;
    int quantity;

    public Cart(CartDTO cartDTO) {
        this.userId = cartDTO.getUserId();
        this.bookId = cartDTO.getBookId();
        this.quantity = cartDTO.getQuantity();
    }
}
