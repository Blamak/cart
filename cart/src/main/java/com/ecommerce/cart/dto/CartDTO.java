package com.ecommerce.cart.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CartDTO {
    private String id;
    private List<ProductDTO> products;
    private LocalDateTime lastUpdated;


	public CartDTO(String id, List<ProductDTO> products, LocalDateTime lastUpdated) {
        this.id = id;
        this.products = products;
        this.lastUpdated = lastUpdated;
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
    
}
