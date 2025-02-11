package com.ecommerce.cart.exception;

public class InvalidProductQuantityException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidProductQuantityException(String mensaje) {
        super(mensaje);
    }
}
