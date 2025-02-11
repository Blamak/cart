package com.ecommerce.cart.service;

import com.ecommerce.cart.dto.CartDTO;

import java.util.List;

public interface CartService {
    CartDTO createCart();
    CartDTO getCart(String cartId);
    List<CartDTO> getAllCarts();
    boolean addProductToCart(String cartId, Long productId);
    void cleanInactiveCarts();
    void deleteCart(String cartId);
}
