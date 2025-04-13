package com.example.bikerentalcu;

import java.io.Serializable;

public class bikeModel implements Serializable {
    private String name;
    private int price;
    private String transmission;
    private String speed;
    private String mileage;
    private String ownerName;
    private String ownerEmail;
    private String ownerContact;
    private String imageUrl;
    private String ownerImageUrl;
    private String ownerupi;

    public bikeModel(String name, int price, String transmission, String speed, String mileage,
                     String ownerName, String ownerEmail, String ownerContact,
                     String imageUrl, String ownerImageUrl, String ownerupi) {
        this.name = name;
        this.price = price;
        this.transmission = transmission;
        this.speed = speed;
        this.mileage = mileage;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.ownerContact = ownerContact;
        this.imageUrl = imageUrl;
        this.ownerImageUrl = ownerImageUrl;
        this.ownerupi = ownerupi;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getTransmission() {
        return transmission;
    }

    public String getSpeed() {
        return speed;
    }

    public String getMileage() {
        return mileage;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public String getOwnerContact() {
        return ownerContact;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getownerurl() {
        return ownerImageUrl;
    }

    public String getownerupi() {
        return ownerupi;
    }
}
