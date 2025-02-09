package com.ecommerce.cart.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class CartModelTest {

	@Test
	void isInactiveFor_ShouldReturnTrue_WhenLastUpdatedExceedsThreshold() {
	    Cart cart = new Cart("123");
	    cart.setLastUpdated(LocalDateTime.now().minusMinutes(15));

	    assertTrue(cart.isInactiveFor(10));
	}

	@Test
	void isInactiveFor_ShouldReturnFalse_WhenLastUpdatedIsRecent() {
	    Cart cart = new Cart("123");
	    cart.setLastUpdated(LocalDateTime.now().minusMinutes(5));

	    assertFalse(cart.isInactiveFor(10));
	}

	
}
