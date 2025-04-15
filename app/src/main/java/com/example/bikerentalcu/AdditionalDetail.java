package com.example.bikerentalcu;

import java.util.List;

public class AdditionalDetail {
    private String _id;
    private String fullName;
    private String email;
    private String gender;
    private String dateofBirth;
    private String about;
    private String contactNumber;
    private String upiId;
    private List<Bike> bikesCreated;
    private List<Bike> bikesRented;

    @Override
    public String toString() {
        return "AdditionalDetail{" +
                "_id='" + _id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", dateofBirth='" + dateofBirth + '\'' +
                ", about='" + about + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", upiId='" + upiId + '\'' +
                ", bikesCreated=" + bikesCreated +
                ", bikesRented=" + bikesRented +
                '}';
    }

    // Getters and Setters
    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateofBirth() {
        return dateofBirth;
    }

    public void setDateofBirth(String dateofBirth) {
        this.dateofBirth = dateofBirth;
    }

    // ✅ Preferred getter
    public String getDateOfBirth() {
        return dateofBirth;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public List<Bike> getBikesCreated() {
        return bikesCreated;
    }

    public void setBikesCreated(List<Bike> bikesCreated) {
        this.bikesCreated = bikesCreated;
    }

    public List<Bike> getBikesRented() {
        return bikesRented;
    }

    public void setBikesRented(List<Bike> bikesRented) {
        this.bikesRented = bikesRented;
    }
}
