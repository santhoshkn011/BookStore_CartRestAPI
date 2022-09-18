package com.example.bookstore_cart;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class BookStoreCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookStoreCartApplication.class, args);
        System.out.println("--------------------------------");
        log.info("\nHello! Welcome to Book Store Project!");
    }

}
