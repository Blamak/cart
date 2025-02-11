package com.ecommerce.cart.service;

import com.ecommerce.cart.dto.CartDTO;
import com.ecommerce.cart.dto.ProductDTO;
import com.ecommerce.cart.exception.CartNotFoundException;
import com.ecommerce.cart.exception.InsufficientStockException;
import com.ecommerce.cart.exception.InvalidProductQuantityException;
import com.ecommerce.cart.exception.ProductNotFoundException;
import com.ecommerce.cart.model.Cart;
import com.ecommerce.cart.model.Product;
import com.ecommerce.cart.repository.CartRepository;
import com.ecommerce.cart.repository.ProductRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

	@Mock
	private CartRepository cartRepository;
	
	 @Mock
	 private ProductRepository productRepository;

	@InjectMocks
	private VolatileCartService cartService;
	

	@Test
    void createCart_ShouldReturnNewCart() {
        CartDTO createdCart = cartService.createCart();

        assertNotNull(createdCart);
        assertNotNull(createdCart.getId());
        verify(cartRepository, times(1)).save(any());
    }

	@Test
    void getCart_ShouldReturnCart_WhenExists() {
		String cartId = "test-cart-id";
	    Cart mockCart = mock(Cart.class);
	    when(mockCart.getId()).thenReturn(cartId);
	    when(cartRepository.findById(cartId)).thenReturn(Optional.of(mockCart));

	    CartDTO cartDTO = cartService.getCart(cartId);

	    assertNotNull(cartDTO);
	    assertEquals(cartId, cartDTO.getId());
	    verify(cartRepository).findById(cartId);
	}


	@Test
	void getCart_ShouldThrowCartNotFoundException_WhenCartDoesNotExist() {
		String cartId = "999";
		when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

		CartNotFoundException exception = assertThrows(CartNotFoundException.class, () -> cartService.getCart(cartId));

		assertEquals("Carrito con ID 999 no encontrado.", exception.getMessage());
	}

	@Test
	void cleanInactiveCarts_ShouldRemoveInactiveCarts() {
		String activeCartId = "active-cart";
		String inactiveCartId = "inactive-cart";

		Cart activeCart = new Cart(activeCartId);
		Cart inactiveCart = new Cart(inactiveCartId);

		inactiveCart.setLastUpdated(LocalDateTime.now().minusMinutes(11));
		activeCart.setLastUpdated(LocalDateTime.now().minusMinutes(5));

		Map<String, Cart> cartsInMemory = new HashMap<>();
		cartsInMemory.put(activeCartId, activeCart);
		cartsInMemory.put(inactiveCartId, inactiveCart);

		when(cartRepository.getAllCarts()).thenReturn(Collections.unmodifiableMap(cartsInMemory));

		cartService.cleanInactiveCarts();

		verify(cartRepository, times(1)).deleteById(inactiveCartId);
		verify(cartRepository, never()).deleteById(activeCartId);
	}

	@Test
	void cleanInactiveCarts_ShouldNotRemoveActiveCarts() {
		String activeCartId = "active-cart";
		Cart activeCart = new Cart(activeCartId);
		activeCart.setLastUpdated(LocalDateTime.now().minusMinutes(5));

		Map<String, Cart> cartsInMemory = new HashMap<>();
		cartsInMemory.put(activeCartId, activeCart);

		when(cartRepository.getAllCarts()).thenReturn(Collections.unmodifiableMap(cartsInMemory));

		cartService.cleanInactiveCarts();

		verify(cartRepository, never()).deleteById(anyString());
	}

	@Test
	void cleanInactiveCarts_ShouldHandleEmptyRepository() {
		when(cartRepository.getAllCarts()).thenReturn(Collections.emptyMap());

		cartService.cleanInactiveCarts();

		verify(cartRepository, never()).deleteById(anyString());
	}
	
	@Test
    void getAllCarts_ShouldReturnListOfCarts() {
        List<Cart> mockCarts = new ArrayList<>();
        mockCarts.add(new Cart("cart1"));
        mockCarts.add(new Cart("cart2"));
        
        when(cartRepository.findAll()).thenReturn(mockCarts);
        
        List<CartDTO> carts = cartService.getAllCarts();
        
        assertNotNull(carts);
        assertEquals(2, carts.size());
        verify(cartRepository).findAll();
    }
	
	  @Test
	    void addProductToCart_ShouldAddProduct_WhenValid() {
	        String cartId = "test-cart-id";
	        Long productId = 1L;
	        int quantity = 2;
	        
	        Cart mockCart = new Cart(cartId);
	        Product mockProduct = new Product(productId, "Test Product", 10);
	        
	        when(cartRepository.findById(cartId)).thenReturn(Optional.of(mockCart));
	        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
	        
	        CartDTO updatedCart = cartService.addProductToCart(cartId, productId, quantity);
	        
	        assertNotNull(updatedCart);
	        assertEquals(1, updatedCart.getProducts().size());
	        verify(cartRepository).save(any(Cart.class));
	    }
	    
	    @Test
	    void addProductToCart_ShouldThrowException_WhenStockInsufficient() {
	        String cartId = "test-cart-id";
	        Long productId = 1L;
	        int quantity = 15;
	        
	        Cart mockCart = new Cart(cartId);
	        Product mockProduct = new Product(productId, "Test Product", 10);
	        
	        when(cartRepository.findById(cartId)).thenReturn(Optional.of(mockCart));
	        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
	        
	        assertThrows(InsufficientStockException.class, () -> cartService.addProductToCart(cartId, productId, quantity));
	    }
	    
	    @Test
	    void addProductToCart_ShouldThrowException_WhenQuantityInvalid() {
	        String cartId = "test-cart-id";
	        Long productId = 1L;
	        
	        assertThrows(InvalidProductQuantityException.class, () -> cartService.addProductToCart(cartId, productId, 0));
	    }
	
	    @Test
	    void deleteCart_ShouldRemoveCart_WhenExists() {
	        String cartId = "test-cart-id";
	        Cart mockCart = new Cart(cartId);
	        
	        when(cartRepository.findById(cartId)).thenReturn(Optional.of(mockCart));
	        
	        cartService.deleteCart(cartId);
	        
	        verify(cartRepository).deleteById(cartId);
	    }
	    
	    @Test
	    void deleteCart_ShouldThrowException_WhenCartNotFound() {
	        String cartId = "test-cart-idsdgfs";
	        
	        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());
	        
	        assertThrows(CartNotFoundException.class, () -> cartService.deleteCart(cartId));
	    }
	    
	    @Test
	    void deleteProductsFromCart_ShouldUpdateProductStock_WhenProductsExist() {
	        List<ProductDTO> products = List.of(new ProductDTO(1L, "Product A", 5));
	        Product mockProduct = new Product(1L, "Product A", 10);
	        
	        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));
	        
	        cartService.deleteProductsFromCart(products);
	        
	        assertEquals(15, mockProduct.getAmount());
	        verify(productRepository).findById(1L);
	    }
	    
	    @Test
	    void deleteProductsFromCart_ShouldThrowException_WhenProductNotFound() {
	        List<ProductDTO> products = List.of(new ProductDTO(1L, "Product A", 5));
	        
	        when(productRepository.findById(1L)).thenReturn(Optional.empty());
	        
	        assertThrows(ProductNotFoundException.class, () -> cartService.deleteProductsFromCart(products));
	    }
	
}
