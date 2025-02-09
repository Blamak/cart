package com.ecommerce.cart.repository;

import com.ecommerce.cart.model.Cart;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CartRepository {
    void save(Cart cart);
    Optional<Cart> findById(String cartId);
    void deleteById(String cartId);
    List<Cart> findAll();
    Map<String, Cart> getAllCarts();
}
