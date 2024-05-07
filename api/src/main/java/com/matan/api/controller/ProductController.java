package com.matan.api.controller;

import com.matan.api.model.Product;
import com.matan.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductRepository productRepo;

    @GetMapping("/")
    public Product[] listProducts(){
        return productRepo.listProducts();
    }
//    public Product createProduct(@RequestBody Product product) {
//        return productRepo.saveProduct(product);
//    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        return productRepo.updateProduct(id, productDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productRepo.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}