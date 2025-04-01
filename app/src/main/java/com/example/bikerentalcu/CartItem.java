package com.example.bikerentalcu;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.util.Objects;

public class CartItem implements Parcelable {
    private String name;
    private int price;
    private String transmission;
    private String speed;
    private String mileage;
    private String ownerName;
    private String ownerEmail;
    private String ownerContact;
    private String imageUrl;
    private String ownerUrl;

    public CartItem(String name, int price, String transmission, String speed, String mileage,
                    String ownerName, String ownerEmail, String ownerContact, String imageUrl, String ownerUrl) {
        this.name = name;
        this.price = price;
        this.transmission = transmission;
        this.speed = speed;
        this.mileage = mileage;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.ownerContact = ownerContact;
        this.imageUrl = imageUrl;
        this.ownerUrl = ownerUrl;
    }

    protected CartItem(Parcel in) {
        name = in.readString();
        price = in.readInt();
        transmission = in.readString();
        speed = in.readString();
        mileage = in.readString();
        ownerName = in.readString();
        ownerEmail = in.readString();
        ownerContact = in.readString();
        imageUrl = in.readString();
        ownerUrl = in.readString();
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };

    // Getters
    public String getName() { return name; }
    public int getPrice() { return price; }
    public String getTransmission() { return transmission; }
    public String getSpeed() { return speed; }
    public String getMileage() { return mileage; }
    public String getOwnerName() { return ownerName; }
    public String getOwnerEmail() { return ownerEmail; }
    public String getOwnerContact() { return ownerContact; }
    public String getImageUrl() { return imageUrl; }
    public String getOwnerUrl() {
        Log.d("Owner URL", ownerUrl);
        return ownerUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(price);
        dest.writeString(transmission);
        dest.writeString(speed);
        dest.writeString(mileage);
        dest.writeString(ownerName);
        dest.writeString(ownerEmail);
        dest.writeString(ownerContact);
        dest.writeString(imageUrl);
        dest.writeString(ownerUrl);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return price == cartItem.price && Objects.equals(name, cartItem.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
