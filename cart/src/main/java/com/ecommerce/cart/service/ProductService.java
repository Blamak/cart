package com.ecommerce.cart.service;

import java.util.List;

import com.ecommerce.cart.dto.ProductDTO;

public interface ProductService {
	List<ProductDTO> getAllProducts();
}
