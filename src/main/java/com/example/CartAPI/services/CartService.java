package com.example.CartAPI.services;

import com.example.CartAPI.entity.Cart;
import java.util.List;

public interface CartService {
    List<Cart> findByUserId(String cartId);
	Cart addToCart(Cart cart);
    List<Cart> findAll();
	void deletebycartId(String cartId);
    Integer countbyuid(String uid);
    void deleteAll();
}
