package com.example.bikerentalcu;

public class SignupResult {
    private boolean success;
    private String fullName;
    private String email;
    private String contactNumber;
    private String drivingLicenseNo;
    private String dateOfBirth;
    private String balance;
    private String bookings;
    private String image;
    private String confirmPassword;
    private String message; // Declare message field
    private String otp;

    // Constructor
    public SignupResult(String bookings, boolean success, String fullName, String email,
                        String contactNumber, String dateOfBirth, String drivingLicenseNo,
                        String balance, String image, String confirmPassword, String message,
                        String otp) {
        this.bookings = bookings;
        this.success = success;
        this.fullName = fullName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.dateOfBirth = dateOfBirth;
        this.drivingLicenseNo = drivingLicenseNo;
        this.balance = balance;
        this.image = image;
        this.confirmPassword = confirmPassword;
        this.message = message;
        this.otp = otp;
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getBalance() {
        return balance;
    }

    public String getBookings() {
        return bookings;
    }

    public String getImage() {
        return image;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOtp() {
        return otp;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
