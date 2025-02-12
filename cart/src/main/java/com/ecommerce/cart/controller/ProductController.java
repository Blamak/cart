package com.ecommerce.cart.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.cart.dto.ProductDTO;
import com.ecommerce.cart.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	@Operation(summary = "Obtener todos los productos", description = "Devuelve la información de todos los productos creados al arrancar la app."
			+ "\n\nPermite comprobar como varía la cantidad de artículos disponibles después de"
			+ " añadirlos a un carrito, de eliminar un carrito con artículos o de que que un carrito haya sido eliminado por estar inactivo más de 10 minutos.")
	@ApiResponse(responseCode = "200", description = "Lista de productos obtenida correctamente")
	public List<ProductDTO> getAllProducts() {
		return productService.getAllProducts();
	}
}
