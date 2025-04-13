package com.example.bikerentalcu;

public class Bike {
    private String name;
    private String price;
    private String transmission;
    private String speed;
    private String mileage;
    private String imageUrl;
    private String ownerName;
    private String ownerEmail;
    private String ownerContact;
    private String ownerImageUrl;
    private String ownerupi;

    public Bike(String name, String price, String transmission, String speed, String mileage,
                String imageUrl, String ownerName, String ownerEmail, String ownerContact, String ownerImageUrl,String ownerupi) {
        this.name = name;
        this.price = price;
        this.transmission = transmission;
        this.speed = speed;
        this.mileage = mileage;
        this.imageUrl = imageUrl;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.ownerContact = ownerContact;

        this.ownerupi = ownerupi;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
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

    public String getImageUrl() {
        return imageUrl;
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

    public String getOwnerImageUrl() {
        return ownerImageUrl;
    }
    public String getownerupi() {
        return ownerupi;
    }
}
