package com.ecommerce.cart.service;

import com.ecommerce.cart.dto.CartDTO;
import com.ecommerce.cart.dto.ProductDTO;
import com.ecommerce.cart.model.Cart;
import com.ecommerce.cart.model.Product;
import com.ecommerce.cart.repository.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {

	private static final Logger logger = LoggerFactory.getLogger(CartService.class);
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

  
    public CartDTO createCart() {
        String cartId = UUID.randomUUID().toString();
        logger.info("Creando carrito con ID: {}", cartId);
        Cart cart = new Cart(cartId);
        cartRepository.save(cart);
        logger.info("Creado carrito con ID: {}", cartId);

        return new CartDTO(cart.getId(), new ArrayList<>());
    }

    public Optional<CartDTO> getCart(String cartId) {
        logger.debug("Buscando carrito con ID: {}", cartId);
        
        return cartRepository.findById(cartId)
            .map(cart -> {
                List<ProductDTO> productDTOs = cart.getProducts().stream()
                    .map(product -> new ProductDTO(
                        product.getId(),
                        product.getDescription(),
                        product.getAmount()
                    ))
                    .toList(); 
                
                return new CartDTO(cart.getId(), productDTOs);
            });
    }

    
    public List<CartDTO> getAllCarts() {
        logger.debug("Listando todos los carritos");

        return cartRepository.findAll().stream()
            .map(cart -> new CartDTO(
                cart.getId(),
                cart.getProducts().stream()
                    .map(product -> new ProductDTO(
                        product.getId(),
                        product.getDescription(),
                        product.getAmount()
                    ))
                    .toList()
            ))
            .toList();
    }

    public boolean addProductToCart(String cartId, Product product) {
        logger.info("Agregando producto {} al carrito {}", product.getId(), cartId);
        
        try {
            return cartRepository.findById(cartId)
                    .map(cart -> {
                        cart.addProduct(product);
                        cart.setLastUpdated(LocalDateTime.now());
                        cartRepository.save(cart);
                        logger.info("Producto agregado con éxito al carrito {}", cartId);
                        return true;
                    })
                    .orElseGet(() -> {
                        logger.warn("No se encontró el carrito {}", cartId);
                        return false;
                    });
        } catch (Exception e) {
            logger.error("Error al agregar producto al carrito {}: {}", cartId, e.getMessage(), e);
            return false;
        }
    }
    
    public void cleanInactiveCarts() {
    	List<Cart> cartsToCheck= new ArrayList<>(cartRepository.getAllCarts().values());
    	int removedCount = 0; 

        logger.info("Revisando {} carritos para eliminar los inactivos...", cartsToCheck.size());

        for (Cart cart : cartsToCheck) {
            boolean isInactive = cart.isInactiveFor(2);
            if (isInactive) {
            	String cartId = cart.getId(); 
                cartRepository.deleteById(cart.getId());
                logger.info("Eliminado carrito inactivo: {}", cartId);
                removedCount++;
            }
        }
        
        logger.info("Proceso de limpieza finalizado. Carritos eliminados: {}", removedCount);
    	
    }

    public void deleteCart(String cartId) {
    	try {
            cartRepository.deleteById(cartId);
            logger.info("Eliminado el carrito {}", cartId);
        } catch (Exception e) {
            logger.error("Error al eliminar el carrito {}: {}", cartId, e.getMessage(), e);
        }
    }

}
