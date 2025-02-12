package com.ecommerce.cart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.ecommerce.cart.dto.ProductDTO;
import com.ecommerce.cart.model.Product;
import com.ecommerce.cart.repository.ProductRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private VolatileProductService productService;

    @BeforeEach
    void setUp() {
        productService = new VolatileProductService(productRepository);
    }

    @Test
    void getAllProducts_ShouldReturnListOfProducts_WhenProductsExist() {
        List<Product> products = Arrays.asList(
                new Product(1L, "Pala de pádel", 15),
                new Product(2L, "Pelotas de pádel (Pack de 3)", 50)
        );

        when(productRepository.findAll()).thenReturn(products);

        List<ProductDTO> result = productService.getAllProducts();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Pala de pádel", result.get(0).getDescription());
        assertEquals(15, result.get(0).getAmount());
    }

    @Test
    void getAllProducts_ShouldReturnEmptyList_WhenNoProductsExist() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        List<ProductDTO> result = productService.getAllProducts();

        assertTrue(result.isEmpty());
    }
}
