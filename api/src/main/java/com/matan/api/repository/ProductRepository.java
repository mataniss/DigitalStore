package com.matan.api.repository;

import com.matan.api.exceptions.BadRequestException;
import com.matan.api.exceptions.UnauthorizedException;
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
    /*
    The method gets a product object and a jwt that the user sent, validates the quantity,
    and adds a new product to the db. The publisher id will be extracted from the jwt.
    If the quantity is negative, code 400 will be returned.
     */
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

    /*
    The method get a product id and deletes the product with the id that was received from the db.
     */
    public void deleteProduct(Long id) throws SQLException {
        DBManager.deleteRowById("PRODUCTS",id);
    }
    /*
    The function gets a product id and updates the quantity of this product in the db
    based on the quantity that was sent.
     */
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
    /*
    The function updates the image property of a product in the db based on the arguments
    that were sent.
     */
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
    /*
    The function gets a product id, a product object and a jwt. The function extracts user id from the
    jwt and checks if he owns the product id that was received. If true, the product properties
    will be updated based on the product object that was received.
     */
    public void updateProduct(Long id, Product product, String Authorization) throws SQLException {
        Long publisherID = Utils.validateJWT(Authorization);
        Product productInDB = getProduct(id);
        if(productInDB.getPublisherID() == publisherID){
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
        else{
            throw new UnauthorizedException("You are not authorized to update this product");
        }

    }
    /*
    The method get a product id and returns a product object from the db with the id that was received.
     */
    public Product getProduct(Long id) throws SQLException {
        Product product = null;
        String sqlStatement = String.format("SELECT * FROM PRODUCTS WHERE id = %s", id);
        ArrayList<Product>  products = listProducts(sqlStatement);
        if(products.size() > 0) {
            product =  products.get(0);
        }
        return product;
    }

    /*
    The function returns an arraylist of products with all the products for sale in the db
     */
    public ArrayList<Product> listProducts() throws SQLException {
        return listProducts("SELECT * FROM PRODUCTS") ;
    }
    /*
    The method gets a sql statement and returns an arraylist of products with all the products
    that match that sql query
     */
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


    /*
    The function gets a jwt, a file and a product id. The user id will be extracted from the jwt,
    and if he own the product, the image will be saved on the server and the file name that was generated
    will be updated in the db for the product with this id.
     */
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