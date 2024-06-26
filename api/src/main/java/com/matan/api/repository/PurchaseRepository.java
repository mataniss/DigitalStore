package com.matan.api.repository;

import com.matan.api.exceptions.BadRequestException;
import com.matan.api.managers.DBManager;
import com.matan.api.model.Product;
import com.matan.api.model.Purchase;
import com.matan.api.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class PurchaseRepository {
    @Autowired
    private ProductRepository productRepo;
    /*
    The function gets a productId, quantity and a jwt token. The function validates all the
    arguments that were received and performs a purchase - if there will be still items left after this
    purchase - the function will update the quantity of the product in the db.
    If the stock will be over after this purchase for this item the product will be removed from the
    catalog.
     */
    public Long makePurchase(Long productID, int quantity, String Authorization) throws SQLException {
        Long generatedID = null;
        Long buyerID = Utils.validateJWT(Authorization);
        if(quantity<0)
            throw new BadRequestException("Quantity cannot be negative");
        Product product = productRepo.getProduct(productID);
        if(product==null)
            throw new BadRequestException("Product not found");
        if(buyerID == product.getPublisherID()){
            throw new BadRequestException("You can't buy your own product.");
        }
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
    /*
    The function get a product object, quantity and buyerID, and adds a new purchase record
    in the db with the arguments that were sent.
     */
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
        pstmt.setInt(6, quantity);
        pstmt.setString(7, Utils.getCurrentDateTime());
        pstmt.setLong(8, buyerID);
        // Execute the insert operation
        Long purchaseID = DBManager.performInsertAndGetGeneratedID(pstmt);
        return purchaseID;
    }
    /*
    The function returns an arraylist of purchases with all the purchases that exits on the db.
     */
    public ArrayList<Purchase> listPurchases() throws SQLException {
        ArrayList<Purchase> purchases = new ArrayList<Purchase>();
        ResultSet rs = DBManager.executeQuery("SELECT * FROM PURCHASES");
        while (rs.next()) {
            Long id =  rs.getLong("id");
            String name = rs.getString("name");
            String description = rs.getString("description");
            String image = rs.getString("image");
            Integer quantity = rs.getInt("quantity");
            Double price = rs.getDouble("price");
            String date = rs.getString("date");
            Long publisherID = rs.getLong("publisherID");
            Long buyerID = rs.getLong("buyerID");
            Purchase newPurchase = new Purchase(id, name, description, image, date, publisherID, price, quantity, buyerID);
            purchases.add(newPurchase);
        }
        return purchases;
    }
}
