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
    private String date_of_birth;

    private AdditionalDetail additionalDetail; // ✅ Added this line

    // ✅ Add Getter for AdditionalDetail
    public AdditionalDetail getAdditionalDetail() {
        return additionalDetail;
    }

    // Other Getters
    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getDrivingLicenseNo() {
        return drivingLicenseNo;
    }

    public String getImage() {
        return image;
    }

    public String getdate_of_birth() {
        return date_of_birth;
    }
}
