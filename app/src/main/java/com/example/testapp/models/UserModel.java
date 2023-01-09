package com.example.testapp.models;

public class UserModel {
    private String image;
    private String firstname;
    private String lastname;
    private String email;

    public UserModel(String image, String firstname, String lastname, String email) {
        this.image = image;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
