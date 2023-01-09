package com.example.testapp.configs;

import android.app.Application;
import android.content.SharedPreferences;

import javax.inject.Inject;

public class PreferencesHelper {

    public @Inject
    SharedPreferences sharedPreferences;

    @Inject
    public PreferencesHelper(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    Application application;

    public String getImage() {
        return sharedPreferences.getString("image", "");
    }

    public void setImage(String image) {
        sharedPreferences.edit().putString("image", image).apply();
    }

    public String getFirstName() {
        return sharedPreferences.getString("firstname", "");
    }

    public void setFirstName(String firstname) {
        sharedPreferences.edit().putString("firstname", firstname).apply();
    }

    public String getLastName() {
        return sharedPreferences.getString("lastname", "");
    }

    public void setLastName(String lastName) {
        sharedPreferences.edit().putString("lastname", lastName).apply();
    }

    public String getEmail() {
        return sharedPreferences.getString("email", "");
    }

    public void setEmail(String email) {
        sharedPreferences.edit().putString("email", email).apply();
    }

    public void clearAllCaches() {
        sharedPreferences.edit().clear().apply();
    }
}
