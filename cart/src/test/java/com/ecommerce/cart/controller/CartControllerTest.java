package com.ecommerce.cart.controller;

import com.ecommerce.cart.service.CartService;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ecommerce.cart.dto.CartDTO;
import com.ecommerce.cart.exception.CartNotFoundException;
import com.ecommerce.cart.exception.GlobalExceptionHandler;
import com.ecommerce.cart.exception.InsufficientStockException;
import com.ecommerce.cart.exception.InvalidProductQuantityException;
import com.ecommerce.cart.exception.ProductNotFoundException;


@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;
    
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }
    
    @Test
    void createCart_ShouldReturnNewCart() throws Exception {
        CartDTO cartDTO = new CartDTO("cart1", Collections.emptyList(), null);
        when(cartService.createCart()).thenReturn(cartDTO);

        mockMvc.perform(post("/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("cart1"));
    }
    
    @Test
    void getCart_ShouldReturnCart_WhenExists() throws Exception {
        CartDTO cartDTO = new CartDTO("cart1", Collections.emptyList(), null);
        when(cartService.getCart("cart1")).thenReturn(cartDTO);

        mockMvc.perform(get("/cart/cart1"))
                .andExpect(status().isOk());
    }
    
    @Test
    void getCart_ShouldReturnNotFound_WhenCartDoesNotExist() throws Exception {
        String cartId = "95b05370-054b-4cea-8181-de542ac35192";
        String expectedMessage = "Carrito con ID " + cartId + " no encontrado.";

        when(cartService.getCart(cartId)).thenThrow(new CartNotFoundException(cartId));
        mockMvc.perform(get("/cart/" + cartId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(expectedMessage));
    }
    
    @Test
    void getAllCarts_ShouldReturnListOfCarts_WhenCartsExist() throws Exception {
        List<CartDTO> carts = Arrays.asList(
                new CartDTO("cart1", Collections.emptyList(), null),
                new CartDTO("cart2", Collections.emptyList(), null)
        );

        when(cartService.getAllCarts()).thenReturn(carts);

        mockMvc.perform(get("/cart/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("cart1"))
                .andExpect(jsonPath("$[1].id").value("cart2"));
    }

    @Test
    void getAllCarts_ShouldReturnNoContent_WhenNoCartsExist() throws Exception {
        when(cartService.getAllCarts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/cart/all"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void addProductToCart_ShouldReturnOk_WhenProductIsAddedSuccessfully() throws Exception {
        String cartId = "cart1";
        Long productId = 100L;
        int quantity = 2;
        CartDTO updatedCart = new CartDTO(cartId, Collections.emptyList(), null);

        when(cartService.addProductToCart(cartId, productId, quantity)).thenReturn(updatedCart);

        mockMvc.perform(post("/cart/" + cartId + "/product/" + productId + "?quantity=" + quantity))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cartId));
    }
    
    @Test
    void addProductToCart_ShouldReturnNotFound_WhenCartDoesNotExist() throws Exception {
        String cartId = "cart1";
        Long productId = 100L;
        int quantity = 2;

        String expectedMessage = "Carrito con ID " + cartId + " no encontrado.";
        when(cartService.addProductToCart(cartId, productId, quantity))
                .thenThrow(new CartNotFoundException(cartId));

        mockMvc.perform(post("/cart/" + cartId + "/product/" + productId + "?quantity=" + quantity))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(expectedMessage));
    }
    
    @Test
    void addProductToCart_ShouldReturnNotFound_WhenProductDoesNotExist() throws Exception {
        String cartId = "cart1";
        Long productId = 100L;
        int quantity = 2;

        String expectedMessage = "Producto con ID " + productId + " no encontrado.";
        when(cartService.addProductToCart(cartId, productId, quantity))
                .thenThrow(new ProductNotFoundException(productId));

        mockMvc.perform(post("/cart/" + cartId + "/product/" + productId + "?quantity=" + quantity))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(expectedMessage));
    }
    
    @Test
    void addProductToCart_ShouldReturnBadRequest_WhenStockIsInsufficient() throws Exception {
        String cartId = "cart1";
        Long productId = 100L;
        int quantityRequested = 10;
        int stockAvailable = 5;

        String expectedMessage = "Stock insuficiente para el producto " + productId + ". Disponible: " + stockAvailable + ", solicitado: " + quantityRequested;
        when(cartService.addProductToCart(cartId, productId, quantityRequested))
                .thenThrow(new InsufficientStockException(productId, stockAvailable, quantityRequested));

        mockMvc.perform(post("/cart/" + cartId + "/product/" + productId + "?quantity=" + quantityRequested))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(expectedMessage));
    }
    
    @Test
    void addProductToCart_ShouldReturnBadRequest_WhenQuantityIsInvalid() throws Exception {
        String cartId = "cart1";
        Long productId = 100L;
        int quantity = -1;

        String expectedMessage = "La cantidad debe ser superior a 0.";
        when(cartService.addProductToCart(cartId, productId, quantity))
                .thenThrow(new InvalidProductQuantityException("La cantidad debe ser superior a 0."));

        mockMvc.perform(post("/cart/" + cartId + "/product/" + productId + "?quantity=" + quantity))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(expectedMessage));
    }
    
    @Test
    void deleteCart_ShouldReturnOk_WhenCartIsDeletedSuccessfully() throws Exception {
        String cartId = "cart1";
        doNothing().when(cartService).deleteCart(cartId);

        mockMvc.perform(delete("/cart/" + cartId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Carrito eliminado correctamente."));
    }

    @Test
    void deleteCart_ShouldReturnNotFound_WhenCartDoesNotExist() throws Exception {
        String cartId = "cart1";
        doThrow(new CartNotFoundException(cartId))
                .when(cartService).deleteCart(cartId);

        String expectedMessage = "Carrito con ID " + cartId + " no encontrado.";
        mockMvc.perform(delete("/cart/" + cartId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(expectedMessage));
    }

}
