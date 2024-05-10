package com.matan.api.controller;

import com.matan.api.model.Product;
import com.matan.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductRepository productRepo;

    @GetMapping("/")
    public ArrayList<Product> listProducts() throws SQLException {
        return productRepo.listProducts();
    }
    @GetMapping("/{id}")
    public Product listProducts(@PathVariable Long id) throws SQLException {
        return productRepo.getProduct(id);
    }
    @PostMapping("/")
    public Long createProduct(@RequestBody Product product) throws SQLException {
        return productRepo.saveProduct(product);
    }

    @PutMapping("/{id}")
    public void updateProduct(@PathVariable Long id, @RequestBody Product productDetails) throws SQLException {
         productRepo.updateProduct(id, productDetails);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteProduct(@PathVariable Long id) throws SQLException {
//        productRepo.deleteProduct(id);
//        return ResponseEntity.ok().build();
//    }
}