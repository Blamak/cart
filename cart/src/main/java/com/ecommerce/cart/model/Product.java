package com.ecommerce.cart.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Product {
	@NotNull(message = "El ID del producto no puede ser nulo")
    private Long id;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String description;

    @Min(value = 1, message = "La cantidad debe ser al menos 1")
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
    
    public void setId(Long id) {
    	this.id = id;
    }
    
    public String getDescription() {
    	return description;
    }
    
    public void setDescription(String description) {
    	this.description = description;
    }
    
    public int getAmount() {
    	return amount;
    }
    
    public void setAmount(int amount) {
    	this.amount = amount;
    }
}
