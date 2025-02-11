package com.ecommerce.cart.repository;

import com.ecommerce.cart.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findAll();
    Optional<Product> findById(Long id);
    void updateProductStock(Long id, int newStock);
	void updateProductStockAfterRemovingCart(Long id, int addedAmount);
}
