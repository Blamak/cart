package com.ecommerce.cart.service;

import com.ecommerce.cart.dto.CartDTO;
import com.ecommerce.cart.dto.ProductDTO;
import com.ecommerce.cart.exception.CartNotFoundException;
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

		CartDTO result = cartService.getCart(cartId);

		assertNotNull(result);
		assertEquals(cartId, result.getId());
	}

	@Test
	void getCart_ShouldThrowCartNotFoundException_WhenCartDoesNotExist() {
		String cartId = "999";
		when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

		CartNotFoundException exception = assertThrows(CartNotFoundException.class, () -> cartService.getCart(cartId));

		assertEquals("Carrito con ID 999 no encontrado.", exception.getMessage());
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
