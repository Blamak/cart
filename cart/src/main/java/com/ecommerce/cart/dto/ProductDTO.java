package com.ecommerce.cart.dto;

public class ProductDTO {
    private Long id;
    private String description;
    private int amount;

    public ProductDTO(Long id, String description, int amount) {
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
}
