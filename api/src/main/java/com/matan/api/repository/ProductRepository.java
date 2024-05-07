package com.matan.api.repository;

import org.springframework.beans.factory.annotation.Autowired;
import com.matan.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {
    public Product saveProduct(Product product) {
//        return productRepository.save(product);
        return null;
    }

//    public Optional<Product> getProductById(Long id) {
//        return productRepository.findById(id);
//    }

    public void deleteProduct(Long id) {
//        productRepository.deleteById(id);
    }

    public Product updateProduct(Long id, Product productDetails) {
//        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
//        product.setName(productDetails.getName());
//        product.setPrice(productDetails.getPrice());
//        return productRepository.save(product);
        return null;
    }

    public Product[] listProducts() {
        Product [] products = new Product[2];
        products[0] = new Product(1L,"my product",2);
        products[1] = new Product(2L,"product name",3);
        return products;
    }
}