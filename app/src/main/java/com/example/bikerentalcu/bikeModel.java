package com.example.bikerentalcu;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class bikeModel implements Parcelable {
    private String name;
    private int price;
    private String transmission;
    private String speed;
    private String mileage;
    private String ownerName;
    private String ownerEmail;
    private String ownerContact;
    private String imageUrl;
    private String ownerurl;

    public bikeModel(String name, int price, String transmission, String speed, String mileage,
                     String ownerName, String ownerEmail, String ownerContact, String imageUrl,String ownerurl) {
        this.name = name;
        this.price = price;
        this.transmission = transmission;
        this.speed = speed;
        this.mileage = mileage;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.ownerContact = ownerContact;
        this.imageUrl = imageUrl;
        this.ownerurl=ownerurl;
    }

    protected bikeModel(Parcel in) {
        name = in.readString();
        price = in.readInt();
        transmission = in.readString();
        speed = in.readString();
        mileage = in.readString();
        ownerName = in.readString();
        ownerEmail = in.readString();
        ownerContact = in.readString();
        imageUrl = in.readString();
        ownerurl = in.readString();
    }

    public static final Creator<bikeModel> CREATOR = new Creator<bikeModel>() {
        @Override
        public bikeModel createFromParcel(Parcel in) {
            return new bikeModel(in);
        }

        @Override
        public bikeModel[] newArray(int size) {
            return new bikeModel[size];
        }
    };

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
        Log.d("owner ka url -->",
                ownerurl);
        return ownerurl;
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
        dest.writeString(ownerurl);
    }


}
