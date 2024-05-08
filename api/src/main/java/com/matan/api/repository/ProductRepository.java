package com.matan.api.repository;

import com.matan.api.managers.DBManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.matan.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    public ArrayList<Product> listProducts() {

        ArrayList<Product> products = new ArrayList<Product>();
        try {
            ResultSet rs = DBManager.executeQuery("SELECT * FROM PRODUCTS");
            while (rs.next()) {
                Long id =  rs.getLong("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String image = rs.getString("image");
                Integer quantity = rs.getInt("quantity");
                Double price = rs.getDouble("price");
                String date = rs.getString("date");
                Long publisherID = rs.getLong("publisherID");
                Product newProduct = new Product(id,name,description,image,date,publisherID,quantity,price);
                products.add(newProduct);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

//        Product [] products = new Product[2];
//        products[0] = new Product(1L,"my product",2);
//        products[1] = new Product(2L,"product name",3);
        return products;
    }
}