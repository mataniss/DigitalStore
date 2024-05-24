package com.matan.api.controller;

import com.matan.api.model.Product;
import com.matan.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductRepository productRepo;
    /*
    This route returns a json with an array of all the products in the catalog
     */
    @GetMapping("/")
    public ArrayList<Product> listProducts() throws SQLException {
        return productRepo.listProducts();
    }
    /*
    This route returns a specific product from the catalog based on the id that was recived
     */
    @GetMapping("/{id}")
    public Product listProducts(@PathVariable Long id) throws SQLException {
        return productRepo.getProduct(id);
    }
    /*
    This route enables a user to post a new product in the catalog
     */
    @PostMapping("/")
    public Long createProduct( @RequestHeader String Authorization, @RequestBody Product product) throws SQLException {
        return productRepo.saveProduct(product, Authorization);
    }
    /*
    This route enables a user to update the information of a product he has posted in the past
     */
    @PutMapping("/{id}")
    public void updateProduct(@RequestHeader String Authorization, @PathVariable Long id, @RequestBody Product productDetails) throws SQLException {
         productRepo.updateProduct(id, productDetails, Authorization);
    }
    /*
    This route enables the user to upload an image to a product he posted in the catalog.
    The image will be sent in the body of the request
     */
    @PostMapping("/uploadImage/{id}")
    public String uploadFile(@RequestHeader String Authorization, @RequestParam("file") MultipartFile file, @PathVariable Long id) throws SQLException {
        return productRepo.uploadImage(Authorization, file, id);
    }
}