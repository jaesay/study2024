package com.example.springvirtualthread;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductRepository repository;

    @GetMapping
    public ResponseEntity<List<ProductEntity>> getProducts() throws InterruptedException {
        Thread.sleep(1000);
        log.info("Thread info {}", Thread.currentThread());
        return ResponseEntity.ok(repository.findAll());
    }
}
