package com.ecommerce.cart.repository;

import com.ecommerce.cart.model.Cart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CartRepositoryTest {

    private VolatileCartRepository cartRepository;

    @BeforeEach
    void setUp() {
        cartRepository = new VolatileCartRepository();
    }

    @Test
    void saveAndFindById_ShouldReturnCart_WhenCartExists() {
        Cart cart = new Cart("123");
        cartRepository.save(cart);

        Optional<Cart> retrievedCart = cartRepository.findById("123");

        assertTrue(retrievedCart.isPresent());
        assertEquals("123", retrievedCart.get().getId());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenCartDoesNotExist() {
        Optional<Cart> retrievedCart = cartRepository.findById("999");

        assertFalse(retrievedCart.isPresent());
    }
}
