package com.ecommerce.cart.service;

import com.ecommerce.cart.dto.CartDTO;
import com.ecommerce.cart.dto.ProductDTO;
import com.ecommerce.cart.model.Cart;
import com.ecommerce.cart.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private VolatileCartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCart_ShouldReturnNewCartDTO() {
        CartDTO cart = cartService.createCart();

        assertNotNull(cart);
        assertNotNull(cart.getId());
    }

    @Test
    void getCart_ShouldReturnCart_WhenExists() {
        String cartId = "123";
        Cart cart = new Cart(cartId);
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        Optional<CartDTO> result = cartService.getCart(cartId);

        assertTrue(result.isPresent());
        assertEquals(cartId, result.get().getId());
    }

    @Test
    void getCart_ShouldReturnEmpty_WhenCartDoesNotExist() {
        when(cartRepository.findById("999")).thenReturn(Optional.empty());

        Optional<CartDTO> result = cartService.getCart("999");

        assertFalse(result.isPresent());
    }

    @Test
    void addProductToCart_ShouldReturnTrue_WhenCartExists() {
        String cartId = "123";
        ProductDTO product = new ProductDTO(1L, "Pala de pádel", 1);
        Cart cart = new Cart(cartId);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        boolean result = cartService.addProductToCart(cartId, product);

        assertTrue(result);
        assertEquals(1, cart.getProducts().size());
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void addProductToCart_ShouldReturnFalse_WhenCartDoesNotExist() {
        String cartId = "999";
        ProductDTO product = new ProductDTO(1L, "Pala de pádel", 1);

        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        boolean result = cartService.addProductToCart(cartId, product);

        assertFalse(result);
    }
    
    @Test
    void cleanInactiveCarts_ShouldRemoveInactiveCarts() {
        String activeCartId = "active-cart";
        String inactiveCartId = "inactive-cart";

        Cart activeCart = new Cart(activeCartId);
        Cart inactiveCart = new Cart(inactiveCartId);

        inactiveCart.setLastUpdated(LocalDateTime.now().minusMinutes(11)); 
        activeCart.setLastUpdated(LocalDateTime.now().minusMinutes(5)); 

        Map<String, Cart> cartsInMemory = new HashMap<>();
        cartsInMemory.put(activeCartId, activeCart);
        cartsInMemory.put(inactiveCartId, inactiveCart);

        when(cartRepository.getAllCarts()).thenReturn(Collections.unmodifiableMap(cartsInMemory));

        cartService.cleanInactiveCarts();

        verify(cartRepository, times(1)).deleteById(inactiveCartId);
        verify(cartRepository, never()).deleteById(activeCartId);
    }
    
    @Test
    void cleanInactiveCarts_ShouldNotRemoveActiveCarts() {
        String activeCartId = "active-cart";
        Cart activeCart = new Cart(activeCartId);
        activeCart.setLastUpdated(LocalDateTime.now().minusMinutes(5)); 

        Map<String, Cart> cartsInMemory = new HashMap<>();
        cartsInMemory.put(activeCartId, activeCart);

        when(cartRepository.getAllCarts()).thenReturn(Collections.unmodifiableMap(cartsInMemory));

        cartService.cleanInactiveCarts();

        verify(cartRepository, never()).deleteById(anyString());
    }
    
    @Test
    void cleanInactiveCarts_ShouldHandleEmptyRepository() {
        when(cartRepository.getAllCarts()).thenReturn(Collections.emptyMap());

        cartService.cleanInactiveCarts();

        verify(cartRepository, never()).deleteById(anyString());
    }



}
