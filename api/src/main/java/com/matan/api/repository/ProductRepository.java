package com.matan.api.repository;

import com.matan.api.managers.DBManager;
import com.matan.api.utils.Utils;
import com.matan.api.model.Product;
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
    public Long saveProduct(Product product,String Authorization) throws SQLException {
        if(product.getQuantity()<0)
            throw new Error("Quantity cannot be negative");
        Long publisherID = Utils.validateJWT(Authorization);
        ResultSet generatedKeys = null;
        String sql = "INSERT INTO PRODUCTS (name, description, price, publisherID, image, quantity, date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = DBManager.getDBConnection().prepareStatement(sql);

        // Set parameters for the query
        pstmt.setString(1, product.getName());
        pstmt.setString(2, product.getDescription());
        pstmt.setDouble(3,  product.getPrice());
        pstmt.setLong(4,  publisherID);
        pstmt.setString(5, product.getImage());
        pstmt.setInt(6, product.getQuantity());
        pstmt.setString(7, Utils.getCurrentDateTime());

        // Execute the insert operation
        Long productId = DBManager.performInsertAndGetGeneratedID(pstmt);
        return productId;
    }


    public void deleteProduct(Long id) throws SQLException {
        DBManager.deleteRowById("PRODUCTS",id);
    }

    public void updateProductQuantity(Long id , Integer quantity) throws SQLException {
        if(quantity<0)
            throw new Error("Quantity cannot be negative");
        PreparedStatement pstmt = null;
        String sql = "UPDATE PRODUCTS SET  quantity = ? WHERE id = ?";
        pstmt = DBManager.getDBConnection().prepareStatement(sql);

        // Set parameters for the query
        pstmt.setInt(1, quantity);
        pstmt.setLong(2, id);

        // Execute the update operation
        int affectedRows = pstmt.executeUpdate();

    }

    public void updateProduct(Long id, Product product, String Authorization) throws SQLException {
        if(product.getQuantity()<0)
            throw new Error("Quantity cannot be negative");
        Long publisherID = Utils.validateJWT(Authorization);
        PreparedStatement pstmt = null;
        String sql = "UPDATE PRODUCTS SET name = ?, description = ?, price = ?, publisherID = ?, image = ?, quantity = ?, date = ? WHERE id = ?";
        pstmt = DBManager.getDBConnection().prepareStatement(sql);

        // Set parameters for the query
        pstmt.setString(1, product.getName());
        pstmt.setString(2, product.getDescription());
        pstmt.setDouble(3, product.getPrice());
        pstmt.setLong(4, publisherID);
        pstmt.setString(5, product.getImage());
        pstmt.setInt(6, product.getQuantity());
        pstmt.setString(7, Utils.getCurrentDateTime());
        pstmt.setLong(8, product.getId());

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

    private String getExtension(String filename) {
        return filename.lastIndexOf(".") != -1 ? filename.substring(filename.lastIndexOf(".")) : "";
    }

    public String uploadImage(String Authorization, MultipartFile file, Long productID) throws SQLException {
        Long publisherID = Utils.validateJWT(Authorization);
        //todo: verify that the product exits and if yes update the image
        //file name in the db for the product. also remove the image from
        //the regular insert and update routes
        if (file.isEmpty()) {
            return "Failed to upload empty file";
        }

        try {
            // Generate a unique file name
            String filename = UUID.randomUUID().toString() + getExtension(file.getOriginalFilename());
            Path path = Paths.get(uploadDirectory + File.separator + filename);
            Files.copy(file.getInputStream(), path); // Save the file to the specified directory
            return "File uploaded successfully: " + filename;
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload file due to IOException: " + e.getMessage();
        }


    }
}