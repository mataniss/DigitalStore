package com.matan.api.controller;

import com.matan.api.model.Purchase;
import com.matan.api.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseRepository purchaseRepo;

    @GetMapping("/")
    public ArrayList<Purchase> listProducts() throws SQLException {
        return purchaseRepo.listPurchases();
    }

    @GetMapping("/makePurchase/{id}")
    public Long makePurchase(@RequestParam(name = "quantity") Integer quantity,
                                @RequestHeader String Authorization,
                                @PathVariable Long id) throws SQLException {
        return purchaseRepo.makePurchase(id, quantity, Authorization);
    }

}
