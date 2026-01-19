package com.manav.ecommerce.miniapi.controller;

import com.manav.ecommerce.miniapi.model.Product;
import com.manav.ecommerce.miniapi.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product, @RequestParam Long categoryId) {
        Product createdProduct = productService.addProduct(product, categoryId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }
}
