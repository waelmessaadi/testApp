package com.example.testapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultResponse {
    @SerializedName("name")
    @Expose
    private NameResponse name;
    @SerializedName("picture")
    @Expose
    private ImageResponse picture;
    @SerializedName("email")
    @Expose
    private String email;

    public NameResponse getName() {
        return name;
    }

    public void setName(NameResponse name) {
        this.name = name;
    }

    public ImageResponse getPicture() {
        return picture;
    }

    public void setPicture(ImageResponse picture) {
        this.picture = picture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
