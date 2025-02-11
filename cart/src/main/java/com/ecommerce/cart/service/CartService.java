package com.ecommerce.cart.service;

import com.ecommerce.cart.dto.CartDTO;

import java.util.List;

public interface CartService {
    CartDTO createCart();
    CartDTO getCart(String cartId);
    List<CartDTO> getAllCarts();
    CartDTO addProductToCart(String cartId, Long productId, int quantity);
    void cleanInactiveCarts();
    void deleteCart(String cartId);
}
