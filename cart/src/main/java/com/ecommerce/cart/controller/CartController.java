package com.ecommerce.cart.controller;

import com.ecommerce.cart.dto.CartDTO;
import com.ecommerce.cart.dto.ProductDTO;
import com.ecommerce.cart.service.CartService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    
    @PostMapping
    public ResponseEntity<CartDTO> createCart() {
        CartDTO cart = cartService.createCart();
        return ResponseEntity.ok(cart);
    }

    
    @GetMapping("/{cartId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable String cartId) {
    	return cartService.getCart(cartId)
    			.map(ResponseEntity::ok)
    			.orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        List<CartDTO> carts = cartService.getAllCarts();
        return ResponseEntity.ok(carts);
    }

   
    @PostMapping("/{cartId}/product")
    public ResponseEntity<String> addProductToCart(@PathVariable String cartId, @Valid @RequestBody ProductDTO productDTO) {
        boolean added = cartService.addProductToCart(cartId, productDTO);
        if (added) {
            return ResponseEntity.ok("Producto agregado correctamente.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/{cartId}/product/{productId}")
    public ResponseEntity<String> addProductToCartById(@PathVariable String cartId, @PathVariable Long productId) {
        boolean added = cartService.addProductToCartById(cartId, productId);
        if (added) {
            return ResponseEntity.ok("Producto agregado correctamente.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }


   
    @DeleteMapping("/{cartId}")
    public ResponseEntity<String> deleteCart(@PathVariable String cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.ok("Carrito eliminado correctamente.");
    }
}
