package com.example.bookstore_cart.service;

import com.example.bookstore_cart.dto.BookDataDTO;
import com.example.bookstore_cart.dto.CartDTO;
import com.example.bookstore_cart.dto.UserDataDTO;
import com.example.bookstore_cart.exception.CartException;
import com.example.bookstore_cart.model.Cart;
import com.example.bookstore_cart.repository.CartRepo;
import com.example.bookstore_cart.utility.TokenUtility;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class CartService implements ICartService{
    private String USER_URI = "http://localhost:9090/user/Id/";
    private String BOOK_URI = "http://localhost:9091/book/Id/";
    @Autowired
    CartRepo cartRepo;
    @Autowired
    TokenUtility tokenUtility;
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public Cart addCartData(CartDTO cartDTO) {
        ResponseEntity<UserDataDTO> userDetails = restTemplate.getForEntity(USER_URI+cartDTO.getUserId(), UserDataDTO.class);
        /*
        //Converting to the JSON object
        JSONObject user = new JSONObject(userDetails.getBody());
        //printing User details in JSON Object form
        System.out.println(user);
        System.out.println(user.get("emailAddress"));
        */
        System.out.println(userDetails.getBody());
        ResponseEntity<BookDataDTO> bookDetails = restTemplate.getForEntity(BOOK_URI+cartDTO.getBookId(), BookDataDTO.class);
        System.out.println(bookDetails.getBody());
        if (userDetails.hasBody() && bookDetails.hasBody()) {
            Cart cartDetails = new Cart(cartDTO);
            return cartRepo.save(cartDetails);
        } else {
            throw new CartException("Invalid User Id | Book Id");
        }
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
        Object userDetails = restTemplate.getForObject("localhost:9090/user/getUser/"+token,Object.class);
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
            throw new CartException("Invalid Cart ID | UserData ID");
    }

    @Override
    public String deleteCartByCartId(Long userId, Long cartId) {
        Optional<Cart> cartDetails = cartRepo.findById(cartId);
        if(cartDetails.isPresent() && cartDetails.get().getUserId().equals(userId)){
            cartRepo.deleteByCartId(cartId);
            return "Deleted Cart ID: "+cartId;
        }else {
            throw new CartException("Cart Does not found: Invalid Cart ID or UserData does not exist.");
        }
    }
}
