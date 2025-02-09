package com.ecommerce.cart.controller;

import com.ecommerce.cart.dto.ProductDTO;
import com.ecommerce.cart.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CartControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    void addProductToCart_ShouldReturnOk_WhenSuccessful() throws Exception {
        String cartId = "123";

        when(cartService.addProductToCart(eq(cartId), any(ProductDTO.class))).thenReturn(true); 

        mockMvc.perform(post("/cart/{cartId}/product", cartId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1, \"description\":\"Pala de pádel\", \"amount\":1}"))
                .andExpect(status().isOk());
    }
    
   

    @Test
    void addProductToCart_ShouldReturnNotFound_WhenCartDoesNotExist() throws Exception {
        String cartId = "999";

        when(cartService.addProductToCart(eq(cartId), any(ProductDTO.class))).thenReturn(false);

        mockMvc.perform(post("/cart/{cartId}/product", cartId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1, \"description\":\"Pala de pádel\", \"amount\":1}"))
                .andExpect(status().isNotFound());
    }
}
