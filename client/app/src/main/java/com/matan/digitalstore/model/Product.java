package com.matan.digitalstore.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Objects;

public class Product implements Parcelable {
    private Long id;
    private String name;
    private String description;
    private String image;
    private String date;
    private Long publisherID;
    private double price;
    private int quantity;

    /*This class represents a product in the catalog, it implements the Parcelable class so
    that we would be able to send it as an extra for an activity
     */
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
        Product item = (Product) o;
        return Double.compare(price, item.price) == 0 && quantity == item.quantity && Objects.equals(id, item.id) && Objects.equals(name, item.name) && Objects.equals(description, item.description) && Objects.equals(image, item.image) && Objects.equals(date, item.date) && Objects.equals(publisherID, item.publisherID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, image, date, publisherID, price, quantity);
    }

    @Override
    public String toString() {
        return "Item{" +
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

    // Constructor used for parcel
    protected Product(Parcel in) {
        name = in.readString();
        description = in.readString();
        image = in.readString();
        date = in.readString();
        id = in.readLong();
        publisherID = in.readLong();
        price = in.readDouble();
        quantity = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(image);
        dest.writeString(date);
        dest.writeLong(id);
        dest.writeLong(publisherID);
        dest.writeDouble(price);
        dest.writeInt(quantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Parcelable.Creator
    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

}