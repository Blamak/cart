package com.ecommerce.cart.repository;

import com.ecommerce.cart.exception.ProductNotFoundException;
import com.ecommerce.cart.model.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class VolatileProductRepository implements ProductRepository {
    private final List<Product> products = new CopyOnWriteArrayList<>(List.of(
    		new Product(1L, "Pala de pádel", 15),
    		new Product(2L, "Pelotas de pádel (Pack de 3)", 50),
    		new Product(3L, "Pelotas de pádel (Pack de 12)", 25),
    		new Product(4L, "Bolsa de pádel", 10)
    ));

    @Override
    public List<Product> findAll() {
        return products;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return products.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    @Override
    public void updateProductStock(Long id, int newStock) {
        findById(id).ifPresent(product -> product.setAmount(newStock));
    }

    @Override
    public void updateProductStockAfterRemovingCart(Long id, int addedAmount) {
    	Product product = findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    	int newStock = product.getAmount() + addedAmount;
    	product.setAmount(newStock);

    }
}
