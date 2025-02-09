package com.ecommerce.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.ecommerce.cart.service.VolatileCartService;


@SpringBootApplication
@EnableScheduling
public class CartApplication {

	public static void main(String[] args) {
		var context = SpringApplication.run(CartApplication.class, args);

        // Start a background thread to clean up inactive carts
        VolatileCartService cartService = context.getBean(VolatileCartService.class);
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10000);
                    System.out.println("cleannn");
                    cartService.cleanInactiveCarts();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
	}

}
