package com.matan.api.repository;

import com.matan.api.managers.DBManager;
import com.matan.api.utils.Utils;
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
    public Long saveProduct(Product product) throws SQLException {
        ResultSet generatedKeys = null;
        Long productId = null;

        String sql = "INSERT INTO PRODUCTS (name, description, price, publisherID, image, quantity, date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = DBManager.getDBConnection().prepareStatement(sql);

        // Set parameters for the query
        pstmt.setString(1, product.getName());
        pstmt.setString(2, product.getDescription());
        pstmt.setDouble(3,  product.getPrice());
        pstmt.setLong(4,  product.getPublisherID());
        pstmt.setString(5, product.getImage());
        pstmt.setInt(6, product.getQuantity());
        pstmt.setString(7, Utils.getCurrentDateTime());

        // Execute the insert operation
        int affectedRows = pstmt.executeUpdate();
        if (affectedRows > 0) {
            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                productId = generatedKeys.getLong(1); // Retrieve the first field of the generated keys (typically the ID)
                System.out.println("Insert successful, product ID: " + productId);
            } else {
                System.out.println("Insert successful, but no ID was returned.");
            }
        } else {
           throw new Error("Insert failed, no rows affected.");
        }
        return productId;
    }

//    public Optional<Product> getProductById(Long id) {
//        return productRepository.findById(id);
//    }

    public void deleteProduct(Long id) throws SQLException {
        //todo: verify user identity before deleting
        DBManager.deleteRowById("PRODUCTS",id);
    }

    public void updateProduct(Long id, Product product) throws SQLException {
        PreparedStatement pstmt = null;

        String sql = "UPDATE PRODUCTS SET name = ?, description = ?, price = ?, publisherID = ?, image = ?, quantity = ?, date = ? WHERE id = ?";
        pstmt = DBManager.getDBConnection().prepareStatement(sql);

        // Set parameters for the query
        pstmt.setString(1, product.getName());
        pstmt.setString(2, product.getDescription());
        pstmt.setDouble(3, product.getPrice());
        pstmt.setLong(4, product.getPublisherID());
        pstmt.setString(5, product.getImage());
        pstmt.setInt(6, product.getQuantity());
        pstmt.setString(7, Utils.getCurrentDateTime());
        pstmt.setLong(8, product.getId());

        // Execute the update operation
        int affectedRows = pstmt.executeUpdate();

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