package com.matan.api.model;

import java.util.Objects;

//This class represents a product in the db
public class Product {
    private Long id;
    private String name;
    private String description;
    private String image;
    private String date;
    private Long publisherID;
    private double price;
    private int quantity;

    public Product(Long id, String name, String description, String image, String date, Long publisherID, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.date = date;
        this.publisherID = publisherID;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getPublisherID() {
        return publisherID;
    }

    public void setPublisherID(Long publisherID) {
        this.publisherID = publisherID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
        Product product = (Product) o;
        return Double.compare(price, product.price) == 0 && quantity == product.quantity && Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(description, product.description) && Objects.equals(image, product.image) && Objects.equals(date, product.date) && Objects.equals(publisherID, product.publisherID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, image, date, publisherID, price, quantity);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", date='" + date + '\'' +
                ", publisherID=" + publisherID +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
