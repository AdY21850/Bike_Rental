package com.example.bikerentalcu;

public class AdditionalDetail {
    private String _id;
    private String gender;
    private String dateofBirth;
    private String about;

    @Override
    public String toString() {
        return "AdditionalDetail{" +
                "_id='" + _id + '\'' +
                ", gender='" + gender + '\'' +
                ", dateofBirth='" + dateofBirth + '\'' +
                ", about='" + about + '\'' +
                '}';
    }

    // Getters and Setters
    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
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

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDateOfBirth() {
        return "DoB";
    }
}
