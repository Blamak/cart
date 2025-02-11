package com.ecommerce.cart.exception;

public class CartNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CartNotFoundException(String cartId) {
        super("Carrito con ID " + cartId + " no encontrado.");
    }
}
