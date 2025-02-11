package com.ecommerce.cart.controller;

import com.ecommerce.cart.dto.CartDTO;
import com.ecommerce.cart.exception.CartNotFoundException;
import com.ecommerce.cart.exception.InsufficientStockException;
import com.ecommerce.cart.exception.InvalidProductQuantityException;
import com.ecommerce.cart.exception.ProductNotFoundException;
import com.ecommerce.cart.service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

	private final CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	@PostMapping
	@Operation(summary = "Crear un nuevo carrito", description = "Genera un nuevo carrito vacío")
	@ApiResponse(responseCode = "200", description = "Carrito creado exitosamente")
	public ResponseEntity<CartDTO> createCart() {
		CartDTO cart = cartService.createCart();
		return ResponseEntity.ok(cart);
	}

	@GetMapping("/{cartId}")
	@Operation(summary = "Obtener un carrito por ID", description = "Devuelve la información del carrito dado su ID")
	@ApiResponse(responseCode = "200", description = "Carrito encontrado")
	@ApiResponse(responseCode = "404", description = "Carrito no encontrado")
	public ResponseEntity<CartDTO> getCart(@PathVariable String cartId) {
		return ResponseEntity.ok(cartService.getCart(cartId));
	}

	@GetMapping("/all")
	@Operation(summary = "Obtener todos los carritos", description = "Devuelve la información de todos los carritos activos")
	@ApiResponse(responseCode = "200", description = "Lista de carritos obtenida correctamente")
	@ApiResponse(responseCode = "204", description = "No hay carritos disponibles")
	public ResponseEntity<List<CartDTO>> getAllCarts() {
		List<CartDTO> carts = cartService.getAllCarts();
		return carts.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(carts);
	}

	@PostMapping("/{cartId}/product/{productId}")
	@Operation(summary = "Agregar un producto al carrito con cantidad específica", description = "Añade un producto al carrito especificando la cantidad deseada")
	@ApiResponse(responseCode = "200", description = "Producto agregado correctamente")
	@ApiResponse(responseCode = "404", description = "Carrito o producto no encontrado")
	@ApiResponse(responseCode = "400", description = "Stock insuficiente o cantidad inválida")
	public ResponseEntity<?> addProductToCart(@PathVariable String cartId, @PathVariable Long productId,
			@RequestParam(defaultValue = "1") int quantity) {
		try {
			CartDTO updatedCart = cartService.addProductToCart(cartId, productId, quantity);
			return ResponseEntity.ok(updatedCart);
		} catch (CartNotFoundException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
	    } catch (ProductNotFoundException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
	    } catch (InsufficientStockException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
	    } catch (InvalidProductQuantityException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
	    }
	}

	@DeleteMapping("/{cartId}")
	@Operation(summary = "Eliminar un carrito", description = "Elimina un carrito dado su ID")
	@ApiResponse(responseCode = "200", description = "Carrito eliminado correctamente")
	@ApiResponse(responseCode = "404", description = "Carrito no encontrado")
	public ResponseEntity<String> deleteCart(@PathVariable String cartId) {
		cartService.deleteCart(cartId);
		return ResponseEntity.ok("Carrito eliminado correctamente.");
	}
}
