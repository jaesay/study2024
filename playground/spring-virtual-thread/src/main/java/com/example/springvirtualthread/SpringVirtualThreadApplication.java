package com.example.springvirtualthread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class SpringVirtualThreadApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringVirtualThreadApplication.class, args);
    }

    @Autowired
    private ProductRepository repository;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        repository.save(ProductEntity.create("product1", 10));
        repository.save(ProductEntity.create("product2", 20));
        repository.save(ProductEntity.create("product3", 30));
        repository.save(ProductEntity.create("product4", 40));
        repository.save(ProductEntity.create("product5", 50));
    }
}
