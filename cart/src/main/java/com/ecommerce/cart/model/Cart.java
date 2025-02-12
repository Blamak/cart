package com.ecommerce.cart.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ecommerce.cart.dto.ProductDTO;

public class Cart {
    private String id; 
    private List<ProductDTO> products;
    private LocalDateTime lastUpdated;

    public Cart(String id) {
        this.id = id;
        this.products = new ArrayList<>();
        this.lastUpdated = LocalDateTime.now();
    }
    
    public String getId() {
		return id;
	}

	public List<ProductDTO> getProducts() {
		return products;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	public void addProduct(Long productId, String description, int quantity) {
	    Optional<ProductDTO> existingProduct = this.products.stream()
	        .filter(p -> p.getId().equals(productId))
	        .findFirst();

	    if (existingProduct.isPresent()) {
	        // Si el producto ya existe en el carrito, solo aumenta la cantidad en el carrito
	        existingProduct.get().setAmount(existingProduct.get().getAmount() + quantity);
	    } else {
	        // Si no existe en el carrito, lo agregamos con la cantidad seleccionada
	        this.products.add(new ProductDTO(this.id, productId, description, quantity));
	    }

	    this.lastUpdated = LocalDateTime.now();
	}
	
	public boolean isInactiveFor(int minutes) {
        return LocalDateTime.now().minusMinutes(minutes).isAfter(lastUpdated);
    }

	
}