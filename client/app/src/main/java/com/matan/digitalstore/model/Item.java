package com.matan.digitalstore.model;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.Objects;

public class Item {
    private Long id;
    private String name;
    private String description;
    private String image;
    private String date;
    private Long publisherID;
    private double price;


    public Item(Long id, String name, String description, String image, String date, Long publisherID, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.date = date;
        this.publisherID = publisherID;
        this.price = price;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Double.compare(price, item.price) == 0 && Objects.equals(id, item.id) && Objects.equals(name, item.name) && Objects.equals(description, item.description) && Objects.equals(image, item.image) && Objects.equals(date, item.date) && Objects.equals(publisherID, item.publisherID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, image, date, publisherID, price);
    }

    @Override
    public String toString() {
        return  "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", date='" + date + '\'' +
                ", publisherID=" + publisherID +
                ", price=" + price + ",";
    }
}