package com.ecommerce.cart.service;

import com.ecommerce.cart.model.Cart;
import com.ecommerce.cart.model.Product;
import com.ecommerce.cart.repository.CartRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
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
    
    public void cleanInactiveCarts() {
    	List<Cart> cartsToRemove = new ArrayList<>(cartRepository.getAllCarts().values());

        for (Cart cart : cartsToRemove) {
            boolean isInactive = cart.isInactiveFor(2);
            System.out.println("Cart ID: " + cart.getId() + " | Inactive: " + isInactive);

            if (isInactive) {
                cartRepository.deleteById(cart.getId());
            }
        }
    	
    }

    public void deleteCart(String cartId) {
        cartRepository.deleteById(cartId);
    }

}
