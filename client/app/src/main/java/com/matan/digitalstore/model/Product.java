package com.matan.digitalstore.model;

import java.util.Objects;

public class Product extends Item {
    private int quantity;

    public Product(Long id, String name, String description, String image, String date, Long publisherID, double price, int quantity) {
        super(id, name, description, image, date, publisherID, price);
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Product product = (Product) o;
        return quantity == product.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), quantity);
    }

    @Override
    public String toString() {
        return "Product{" + super.toString() +
                "quantity=" + quantity +
                '}';
    }
}
