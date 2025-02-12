package com.ecommerce.cart.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.cart.dto.ProductDTO;
import com.ecommerce.cart.repository.ProductRepository;

@Service
public class VolatileProductService implements ProductService{

	private final ProductRepository productRepository;
	
	public VolatileProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public List<ProductDTO> getAllProducts() {
		return productRepository.findAll().stream()
				.map(product -> new ProductDTO(product.getId(), product.getDescription(), product.getAmount()))
				.toList();
	}
	
}
