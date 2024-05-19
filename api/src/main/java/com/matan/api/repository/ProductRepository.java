package com.matan.api.repository;

import com.matan.api.exceptions.BadRequestException;
import com.matan.api.managers.DBManager;
import com.matan.api.model.Product;
import com.matan.api.utils.Utils;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class ProductRepository {

    public static final String uploadDirectory = "public/images";
    public Long saveProduct(Product product, String Authorization) throws SQLException {
        Long publisherID = Utils.validateJWT(Authorization);
        if(product.getQuantity()<0)
            throw new BadRequestException("Quantity cannot be negative");
        ResultSet generatedKeys = null;
        String sql = "INSERT INTO PRODUCTS (name, description, price, publisherID, quantity, date) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = DBManager.getDBConnection().prepareStatement(sql);

        // Set parameters for the query
        pstmt.setString(1, product.getName());
        pstmt.setString(2, product.getDescription());
        pstmt.setDouble(3,  product.getPrice());
        pstmt.setLong(4,  publisherID);
        pstmt.setInt(5, product.getQuantity());
        pstmt.setString(6, Utils.getCurrentDateTime());

        // Execute the insert operation
        Long productId = DBManager.performInsertAndGetGeneratedID(pstmt);
        return productId;
    }


    public void deleteProduct(Long id) throws SQLException {
        DBManager.deleteRowById("PRODUCTS",id);
    }

    public void updateProductQuantity(Long id , Integer quantity) throws SQLException {
        if(quantity<0)
            throw new BadRequestException("Quantity cannot be negative");
        PreparedStatement pstmt = null;
        String sql = "UPDATE PRODUCTS SET  quantity = ? WHERE id = ?";
        pstmt = DBManager.getDBConnection().prepareStatement(sql);

        // Set parameters for the query
        pstmt.setInt(1, quantity);
        pstmt.setLong(2, id);

        // Execute the update operation
        int affectedRows = pstmt.executeUpdate();

    }

    public void updateProductImage(Long id , String image) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = "UPDATE PRODUCTS SET  image = ? WHERE id = ?";
        pstmt = DBManager.getDBConnection().prepareStatement(sql);
        // Set parameters for the query
        pstmt.setString(1, image);
        pstmt.setLong(2, id);
        // Execute the update operation
        int affectedRows = pstmt.executeUpdate();
    }

    public void updateProduct(Long id, Product product, String Authorization) throws SQLException {
        Long publisherID = Utils.validateJWT(Authorization);
        if(product.getQuantity()<0)
            throw new BadRequestException("Quantity cannot be negative");
        PreparedStatement pstmt = null;
        String sql = "UPDATE PRODUCTS SET name = ?, description = ?, price = ?, publisherID = ?, quantity = ?, date = ? WHERE id = ?";
        pstmt = DBManager.getDBConnection().prepareStatement(sql);

        // Set parameters for the query
        pstmt.setString(1, product.getName());
        pstmt.setString(2, product.getDescription());
        pstmt.setDouble(3, product.getPrice());
        pstmt.setLong(4, publisherID);
        pstmt.setInt(5, product.getQuantity());
        pstmt.setString(6, Utils.getCurrentDateTime());
        pstmt.setLong(7, id);

        // Execute the update operation
        int affectedRows = pstmt.executeUpdate();

    }

    public Product getProduct(Long id) throws SQLException {
        Product product = null;
        String sqlStatement = String.format("SELECT * FROM PRODUCTS WHERE id = %s", id);
        ArrayList<Product>  products = listProducts(sqlStatement);
        if(products.size() > 0) {
            product =  products.get(0);
        }
        return product;
    }


    public ArrayList<Product> listProducts() throws SQLException {
        return listProducts("SELECT * FROM PRODUCTS") ;
    }

    public ArrayList<Product> listProducts(String sqlStatement) throws SQLException {
        ArrayList<Product> products = new ArrayList<Product>();
        ResultSet rs = DBManager.executeQuery(sqlStatement);
        while (rs.next()) {
            Long id =  rs.getLong("id");
            String name = rs.getString("name");
            String description = rs.getString("description");
            String image = rs.getString("image");
            Integer quantity = rs.getInt("quantity");
            Double price = rs.getDouble("price");
            String date = rs.getString("date");
            Long publisherID = rs.getLong("publisherID");
            Product newProduct = new Product(id, name, description, image, date, publisherID, price, quantity);
            products.add(newProduct);
        }
        return products;
    }



    public String uploadImage(String Authorization, MultipartFile file, Long productID) throws SQLException {
        Long publisherID = Utils.validateJWT(Authorization);
        Product product = getProduct(productID);
        if(product.getPublisherID() == publisherID){
            if (file.isEmpty()) {
                return "Failed to upload empty file";
            }

            try {
                // Generate a unique file name
                String filename = UUID.randomUUID().toString() + Utils.getExtension(file.getOriginalFilename());
                Path path = Paths.get(uploadDirectory + File.separator + filename);
                Files.copy(file.getInputStream(), path); // Save the file to the specified directory
                updateProductImage(productID, filename);
                return "File uploaded successfully: " + filename;
            } catch (IOException e) {
                e.printStackTrace();
                return "Failed to upload file due to IOException: " + e.getMessage();
            }
        }
        else {
            throw new Error("You are not allowed to upload a file for a product that isn't yours!");
        }


    }
}