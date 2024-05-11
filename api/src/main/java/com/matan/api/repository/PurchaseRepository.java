package com.matan.api.repository;

import com.matan.api.managers.DBManager;
import com.matan.api.model.Product;
import com.matan.api.model.Purchase;
import com.matan.api.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class PurchaseRepository {
    @Autowired
    private ProductRepository productRepo;

    public Long makePurchase(Long productID, int quantity, String Authorization) throws SQLException {
        Long generatedID = null;
        if(quantity<0)
            throw new Error("Quantity cannot be negative");
        Long buyerID = Utils.validateJWT(Authorization);
        Product product = productRepo.getProduct(productID);
        if(product.getQuantity() >= quantity){
            //create new purchase record
            generatedID = addNewPurchaseRecord(product,quantity,buyerID);
            if(product.getQuantity() == quantity){
                //the user wants to buy all the available stock,
                //remove product from products table
                productRepo.deleteProduct(productID);
            }
            else {
                //there is still gonna be available stock after this purchase,
                // update the product quantity
                Integer updatedQuantity = product.getQuantity() - quantity;
                productRepo.updateProductQuantity(productID, updatedQuantity);
            }
        }
        return generatedID;
    }

    private Long addNewPurchaseRecord(Product product, int quantity, Long buyerID) throws SQLException {
        ResultSet generatedKeys = null;
        String sql = "INSERT INTO PURCHASES (name, description, price, publisherID, image, quantity, date, buyerID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = DBManager.getDBConnection().prepareStatement(sql);

        // Set parameters for the query
        pstmt.setString(1, product.getName());
        pstmt.setString(2, product.getDescription());
        pstmt.setDouble(3,  product.getPrice());
        pstmt.setLong(4,  product.getPublisherID());
        pstmt.setString(5, product.getImage());
        pstmt.setInt(6, product.getQuantity());
        pstmt.setString(7, Utils.getCurrentDateTime());
        pstmt.setLong(8, buyerID);
        // Execute the insert operation
        Long purchaseID = DBManager.performInsertAndGetGeneratedID(pstmt);
        return purchaseID;
    }
}
