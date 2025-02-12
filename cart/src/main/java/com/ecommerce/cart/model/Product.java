package com.ecommerce.cart.model;

public class Product {
   
	private Long id;
    private String description;
    private int amount;
    
    public Product() {}

    public Product(Long id, String description, int amount) {
        this.id = id;
        this.description = description;
        this.amount = amount;
    }
    
    public Long getId() {
    	return id;
    }
    
    public String getDescription() {
    	return description;
    }
    
    public int getAmount() {
    	return amount;
    }
    
    public void setAmount(int amount) {
    	this.amount = amount;
    }
}
