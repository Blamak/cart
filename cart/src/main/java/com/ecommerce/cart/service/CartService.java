package com.ecommerce.cart.service;

import com.ecommerce.cart.model.Cart;
import com.ecommerce.cart.model.Product;
import com.ecommerce.cart.repository.CartRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart createCart() {
        String cartId = UUID.randomUUID().toString();
        Cart cart = new Cart(cartId);
        cartRepository.save(cart);
        return cart;
    }

    public Optional<Cart> getCart(String cartId) {
        return cartRepository.findById(cartId);
    }

    public boolean addProductToCart(String cartId, Product product) {
        Optional<Cart> cartOpt = cartRepository.findById(cartId);
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            cart.addProduct(product);
            cart.setLastUpdated(LocalDateTime.now());
            cartRepository.save(cart);
            return true;
        }
        return false;
    }

    public void deleteCart(String cartId) {
        cartRepository.deleteById(cartId);
    }

    public void removeInactiveCarts(int minutes) {
        cartRepository.getAllCarts().values().removeIf(cart -> cart.isInactiveFor(minutes));
    }
}
