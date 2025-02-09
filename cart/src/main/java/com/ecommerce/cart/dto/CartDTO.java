package com.ecommerce.cart.dto;

import java.util.List;

public class CartDTO {
    private String id;
    private List<ProductDTO> products;

    public CartDTO(String id, List<ProductDTO> products) {
        this.id = id;
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }
}
