package com.ecommerce.cart.service;

import com.ecommerce.cart.dto.CartDTO;
import com.ecommerce.cart.dto.ProductDTO;
import com.ecommerce.cart.exception.CartNotFoundException;
import com.ecommerce.cart.exception.ProductNotFoundException;
import com.ecommerce.cart.exception.InsufficientStockException;
import com.ecommerce.cart.exception.InvalidProductQuantityException;
import com.ecommerce.cart.model.Cart;
import com.ecommerce.cart.model.Product;
import com.ecommerce.cart.repository.CartRepository;
import com.ecommerce.cart.repository.ProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class VolatileCartService implements CartService {

	private static final Logger logger = LoggerFactory.getLogger(VolatileCartService.class);
	private final CartRepository cartRepository;
	private final ProductRepository productRepository;

	public VolatileCartService(CartRepository cartRepository, ProductRepository productRepository) {
		this.cartRepository = cartRepository;
		this.productRepository = productRepository;
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
	public CartDTO getCart(String cartId) {
		logger.debug("Buscando carrito con ID: {}", cartId);

		return cartRepository.findById(cartId)
				.map(cart -> new CartDTO(cart.getId(), cart.getProducts().stream()
						.map(product -> new ProductDTO(cartId, product.getId(), product.getDescription(), product.getAmount()))
						.toList(), cart.getLastUpdated()))
				.orElseThrow(() -> new CartNotFoundException(cartId));
	}

	@Override
	public List<CartDTO> getAllCarts() {
		logger.debug("Listando todos los carritos");

		return cartRepository.findAll().stream()
				.map(cart -> new CartDTO(cart.getId(), cart.getProducts().stream()
						.map(product -> new ProductDTO(cart.getId(), product.getId(), product.getDescription(), product.getAmount()))
						.toList(), cart.getLastUpdated()))
				.toList();
	}

	@Override
	public CartDTO addProductToCart(String cartId, Long productId, int quantity) {
		logger.info("QQQ {}", quantity);
		if (quantity <= 0) {
			logger.info("QQQ {}", quantity);
			throw new InvalidProductQuantityException("La cantidad debe ser superior a 0.");
		}
		
		logger.info("Añadiendo {} unidades del producto {} al carrito {}", quantity, productId, cartId);

		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> {logger.warn("Carrito {} no encontrado", cartId);
			return new CartNotFoundException(cartId);
		});

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> {logger.warn("Producto {} no encontrado", productId);
			return new ProductNotFoundException(productId);
		});
		
		ProductDTO productDTO = new ProductDTO(product.getId(), product.getDescription(), product.getAmount());

		if (productDTO.getAmount() < quantity) {
			logger.warn("Stock insuficiente para el producto {}. Disponible: {}, solicitado: {}", product.getDescription(),
					product.getAmount(), quantity);
			throw new InsufficientStockException(productId, productDTO.getAmount(), quantity);
		}

		 int newStock = productDTO.getAmount() - quantity;
		 productRepository.updateProductStock(productId, newStock);

		cart.addProduct(productDTO.getId(), productDTO.getDescription(), quantity);
		cartRepository.save(cart);

		logger.info("Producto {} agregado con éxito al carrito {}. Stock restante: {}", product.getDescription(), cartId,
				product.getAmount());
		
		return new CartDTO(
                cart.getId(),
                cart.getProducts().stream().toList(),
                cart.getLastUpdated()
        );
	}

	@Override
	@Scheduled(fixedRate = 600000)
	public void cleanInactiveCarts() {
		List<Cart> cartsToCheck = new ArrayList<>(cartRepository.getAllCarts().values());
		int removedCount = 0;

		logger.info("Revisando {} carritos para eliminar los inactivos...", cartsToCheck.size());

		for (Cart cart : cartsToCheck) {
			boolean isInactive = cart.isInactiveFor(10);
			if (isInactive) {
				String cartId = cart.getId();
				deleteProductsFromCart(cart.getProducts());
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
			Cart cart = cartRepository.findById(cartId)
					.orElseThrow(() -> new CartNotFoundException(cartId));
			
			deleteProductsFromCart(cart.getProducts());
			cartRepository.deleteById(cartId);
			logger.info("Eliminado el carrito {}", cartId);
		} catch (Exception e) {
			logger.error("Error al eliminar el carrito {}: {}", cartId, e.getMessage(), e);
		}
	}
	
	private void deleteProductsFromCart(List<ProductDTO> products) {
	    products.forEach(productDTO -> {
	        Product product = productRepository.findById(productDTO.getId())
	                .orElseThrow(() -> new ProductNotFoundException(productDTO.getId()));

	        product.setAmount(product.getAmount() + productDTO.getAmount());
	    });
	}

}
