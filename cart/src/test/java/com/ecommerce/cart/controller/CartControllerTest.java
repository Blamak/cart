package com.ecommerce.cart.controller;

import com.ecommerce.cart.service.CartService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

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

   


}
