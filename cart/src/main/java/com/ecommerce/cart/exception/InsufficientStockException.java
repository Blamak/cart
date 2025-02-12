package com.ecommerce.cart.exception;

public class InsufficientStockException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InsufficientStockException(Long productId, int stockAvailable, int quantityRequested) {
        super("Stock insuficiente para el producto " + productId + ". Disponible: " + stockAvailable + ", solicitado: " + quantityRequested);
    }
}
