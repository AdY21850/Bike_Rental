package com.example.bikerentalcu;

public class User {
    private String _id;
    private String fullName;
    private String email;
    private String contactNumber;
    private String drivingLicenseNo;
    private boolean active;
    private String image;
    private String createdAt;
    private String updatedAt;

    // Add Getters
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getContactNumber() { return contactNumber; }

    public String getImage() {return image;
    }

    public String getDrivingLicenseNo() {return drivingLicenseNo;
    }
}
