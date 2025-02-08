package com.ecommerce.cart.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private String id; 
    private List<Product> products;
    private LocalDateTime lastUpdated;

    public Cart() {
        this.products = new ArrayList<>();
        this.lastUpdated = LocalDateTime.now();
    }

    public Cart(String id) {
        this.id = id;
        this.products = new ArrayList<>();
        this.lastUpdated = LocalDateTime.now();
    }
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	 public void addProduct(Product product) {
	        this.products.add(product);
	        this.lastUpdated = LocalDateTime.now();
	 }
	
	public boolean isInactiveFor(int minutes) {
        return LocalDateTime.now().minusMinutes(minutes).isAfter(lastUpdated);
    }

}