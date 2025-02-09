package com.ecommerce.cart.service;

import com.ecommerce.cart.dto.CartDTO;
import com.ecommerce.cart.dto.ProductDTO;

import java.util.List;
import java.util.Optional;

public interface CartService {
    CartDTO createCart();
    Optional<CartDTO> getCart(String cartId);
    List<CartDTO> getAllCarts();
    boolean addProductToCart(String cartId, ProductDTO product);
    void cleanInactiveCarts();
    void deleteCart(String cartId);
}
