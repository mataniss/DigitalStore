package com.matan.api.repository;

import com.matan.api.managers.DBManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.matan.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Repository
public class ProductRepository {
    public Product saveProduct(Product product) throws SQLException {
        // Prepare the SQL statement
        LocalDateTime now = LocalDateTime.now();

        // Create a formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the current date and time
        String formattedDateTime = now.format(formatter);

        String sql = "INSERT INTO PRODUCTS (name, description, price, publisherID, image, quantity, date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = DBManager.getDBConnection().prepareStatement(sql);

        // Set parameters for the query
        pstmt.setString(1, product.getName());
        pstmt.setString(2, product.getDescription());
        pstmt.setDouble(3,  product.getPrice());
        pstmt.setLong(4,  product.getPublisherID());
        pstmt.setString(5, product.getImage());
        pstmt.setInt(6, product.getQuantity());
        pstmt.setString(7,formattedDateTime);

        // Execute the insert operation
        int affectedRows = pstmt.executeUpdate();
        //todo: maybe return the product form the db with id
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

        return products;
    }
}