package com.example.bookstore_cart.service;

import com.example.bookstore_cart.dto.CartDTO;
import com.example.bookstore_cart.exception.CartException;
import com.example.bookstore_cart.model.Cart;
import com.example.bookstore_cart.repository.CartRepo;
import com.example.bookstore_cart.utility.TokenUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService implements ICartService{
    @Autowired
    CartRepo cartRepo;
    @Autowired
    TokenUtility tokenUtility;
    @Override
    public Cart addCartData(CartDTO cartDTO) {
        Cart cartDetails = new Cart(cartDTO);
        return cartRepo.save(cartDetails);
    }

    @Override
    public List<Cart> allCartList() {
        List<Cart> cartList = cartRepo.findAll();
        if(cartList.isEmpty()){
            throw new CartException("No Items added in cart yet!!");
        }else
            return cartList;
    }

    @Override
    public Cart getCartDetailsByCartId(Long cartId) {
        Optional<Cart> cartDetails = cartRepo.findById(cartId);
        if(cartDetails.isPresent()){
            return cartDetails.get();
        }else
            throw new CartException("Cart ID does not exist: Invalid ID");
    }

    @Override
    public List<Cart> getCartDetailsByUserId(Long userId) {
        List<Cart> userCartList = cartRepo.getCartListWithUserId(userId);
        if(userCartList.isEmpty()){
            throw new CartException("Cart is Empty!");
        }else
            return userCartList;    }

    @Override
    public List<Cart> getCartDetailsByToken(String token) {
        Long userId = tokenUtility.decodeToken(token);
        List<Cart> userCartList = cartRepo.getCartListWithUserId(userId);
        if(userCartList.isEmpty()){
            throw new CartException("Cart is Empty!");
        }else
            return userCartList;    }

    @Override
    public String editCartByCartId(Long cartId, CartDTO cartDTO) {
        Optional<Cart> cartDetails = cartRepo.findById(cartId);
        if(cartDetails.isPresent() && cartDetails.get().getUserId().equals(cartDTO.getUserId())){
            cartDetails.get().setBookId(cartDTO.getBookId());
            cartDetails.get().setQuantity(cartDTO.getQuantity());
            cartRepo.save(cartDetails.get());
            return "Cart Details Updated! with Book ID: "+cartDTO.getBookId()+", Quantity: "+cartDTO.getQuantity();
        }else
            throw new CartException("Invalid Cart ID | User ID");
    }

    @Override
    public String deleteCartByCartId(Long userId, Long cartId) {
        Optional<Cart> cartDetails = cartRepo.findById(cartId);
        if(cartDetails.isPresent() && cartDetails.get().getUserId().equals(userId)){
            cartRepo.deleteByCartId(cartId);
            return "Deleted Cart ID: "+cartId;
        }else {
            throw new CartException("Cart Does not found: Invalid Cart ID or User does not exist.");
        }
    }
}
