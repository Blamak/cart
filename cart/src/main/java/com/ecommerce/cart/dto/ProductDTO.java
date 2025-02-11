package com.ecommerce.cart.dto;


public class ProductDTO {
    private Long id;
    private String description;
    private int amount;
    private String cartId;

    public ProductDTO(Long id, String description, int amount) {
        this.id = id;
        this.description = description;
        this.amount = amount;
    }

    public ProductDTO(String cartId, Long id, String description, int amount) {
    	this.cartId = cartId;
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

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public void setAmount(int i) {
		this.amount = i;
		
	}
}
