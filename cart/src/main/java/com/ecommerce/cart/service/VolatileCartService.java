package com.ecommerce.cart.service;

import com.ecommerce.cart.config.DataInitializer;
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
public class VolatileCartService implements CartService {

	private static final Logger logger = LoggerFactory.getLogger(VolatileCartService.class);
    private final CartRepository cartRepository;

    public VolatileCartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public CartDTO createCart() {
        String cartId = UUID.randomUUID().toString();
        logger.info("Creando carrito con ID: {}", cartId);
        Cart cart = new Cart(cartId);
        cartRepository.save(cart);
        logger.info("Creado carrito con ID: {}", cartId);

        return new CartDTO(cart.getId(), new ArrayList<>(), cart.getLastUpdated());
    }

    @Override
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
                
                return new CartDTO(cart.getId(), productDTOs, cart.getLastUpdated());
            });
    }

    @Override
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
                    .toList(),
                    cart.getLastUpdated()
            ))
            .toList();
    }

    @Override
    public boolean addProductToCart(String cartId, ProductDTO productDTO) {
        logger.info("Agregando producto {} al carrito {}", productDTO.getId(), cartId);
        
        try {
            return cartRepository.findById(cartId)
            		.map(cart -> {
            			Product product = new Product(
            					productDTO.getId(),
            					productDTO.getDescription(),
            					productDTO.getAmount()
            			);
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
    
    public boolean addProductToCartById(String cartId, Long productId) {
        logger.info("Añadiendo producto {} al carrito {}", productId, cartId);

        return cartRepository.findById(cartId)
            .map(cart -> {
                // Find the product from the initialized list
                Optional<Product> productOpt = DataInitializer.getProducts().stream()
                        .filter(p -> p.getId().equals(productId))
                        .findFirst();

                if (productOpt.isPresent()) {
                    cart.addProduct(productOpt.get());
                    cart.setLastUpdated(LocalDateTime.now());
                    cartRepository.save(cart);
                    logger.info("Producto {} añadido correctamente al carrito {}", productId, cartId);
                    return true;
                } else {
                    logger.warn("Producto {} no encontrado", productId);
                    return false;
                }
            })
            .orElseGet(() -> {
                logger.warn("Carrito {} no encontrado", cartId);
                return false;
            });
    }

    
    @Override
    public void cleanInactiveCarts() {
    	List<Cart> cartsToCheck= new ArrayList<>(cartRepository.getAllCarts().values());
    	int removedCount = 0; 

        logger.info("Revisando {} carritos para eliminar los inactivos...", cartsToCheck.size());

        for (Cart cart : cartsToCheck) {
            boolean isInactive = cart.isInactiveFor(10);
            if (isInactive) {
            	String cartId = cart.getId(); 
                cartRepository.deleteById(cart.getId());
                logger.info("Eliminado carrito inactivo: {}", cartId);
                removedCount++;
            }
        }
        
        logger.info("Proceso de limpieza finalizado. Carritos eliminados: {}", removedCount);
    	
    }

    @Override
    public void deleteCart(String cartId) {
    	try {
            cartRepository.deleteById(cartId);
            logger.info("Eliminado el carrito {}", cartId);
        } catch (Exception e) {
            logger.error("Error al eliminar el carrito {}: {}", cartId, e.getMessage(), e);
        }
    }


}
