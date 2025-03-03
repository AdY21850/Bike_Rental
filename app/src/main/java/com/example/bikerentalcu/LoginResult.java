package com.example.bikerentalcu;

public class LoginResult {
    private String name;
    private String email;
    private String contactNumber;
    private String token; // Add token field

    public String getName() {
        return name;
    }

    public String contactNumber() {
        return contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() { // Getter for token
        return token;
    }
}
