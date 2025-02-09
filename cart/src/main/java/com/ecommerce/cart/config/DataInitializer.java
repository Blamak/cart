package com.ecommerce.cart.config;

import com.ecommerce.cart.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    private static final List<Product> PRODUCTS;

    static {
        logger.info("Inicializando productos en memoria...");

        PRODUCTS = List.of(
        		new Product(1L, "Pala de pádel", 15),
        		new Product(2L, "Pelotas de pádel (Pack de 3)", 50),
        		new Product(3L, "Pelotas de pádel (Pack de 12)", 25),
        		new Product(4L, "Bolsa de pádel", 10)
        );

        logger.info("Productos inicializados correctamente.");
    }

    public DataInitializer() {}

    public static List<Product> getProducts() {
        return PRODUCTS;
    }
}
