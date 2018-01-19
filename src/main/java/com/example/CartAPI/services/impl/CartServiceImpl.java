package com.example.CartAPI.services.impl;

import com.example.CartAPI.entity.Cart;
import com.example.CartAPI.repository.CartRepository;
import com.example.CartAPI.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartRepository CartRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Cart addToCart(Cart cart) {
        System.out.println(cart);
        return CartRepository.save(cart);
    }

    @Override
    public List<Cart> findByUserId(String userId) {
        return CartRepository.findByUserId(userId);
    }

    @Override
    public List<Cart> findAll() {
        return CartRepository.findAll();
    }

    @Override
    public void deleteAll() {
        CartRepository.deleteAll();

    }

    @Override
	public void deletebycartId(String cartId) {
      CartRepository.delete(cartId);
	}

	@Override
    public Integer countbyuid(String userId){
        return CartRepository.countByUserId(userId);
    }
}
