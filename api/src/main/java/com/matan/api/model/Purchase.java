package com.matan.api.model;

import java.util.Objects;

public class Purchase extends Item{
    private Long buyerID;

    public Purchase(Long id, String name, String description, String image, String date, Long publisherID, double price, Long buyerID) {
        super(id, name, description, image, date, publisherID, price);
        this.buyerID = buyerID;
    }

    public Long getBuyerID() {
        return buyerID;
    }

    public void setBuyerID(Long buyerID) {
        this.buyerID = buyerID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Purchase purchase = (Purchase) o;
        return Objects.equals(buyerID, purchase.buyerID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), buyerID);
    }

    @Override
    public String toString() {
        return "Purchase{" + super.toString() +
                "buyerID=" + buyerID +
                '}';
    }
}
