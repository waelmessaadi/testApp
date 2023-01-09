package com.example.testapp.repository;

import com.example.testapp.configs.PreferencesHelper;
import com.example.testapp.dao.TestAppDao;
import com.example.testapp.db.TestAppDB;
import com.example.testapp.interfaces.TestAppApiServices;
import com.example.testapp.models.UserResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class TestAppRepository {
    private TestAppDao testAppDao;
    private TestAppApiServices apiService;
    private PreferencesHelper preferencesHelper;

    @Inject
    public TestAppRepository(TestAppDao testAppDao, TestAppApiServices apiService, PreferencesHelper preferencesHelper) {
        this.testAppDao = testAppDao;
        this.apiService = apiService;
        this.preferencesHelper = preferencesHelper;
    }

    // Preferences return
    public String getImage() {
        return preferencesHelper.getImage();
    }

    public void setImage(String image) {
        preferencesHelper.setImage(image);
    }

    public String getFirstName() {
        return preferencesHelper.getFirstName();
    }

    public void setFirstName(String firstName) {
        preferencesHelper.setFirstName(firstName);
    }

    public String getLastName() {
        return preferencesHelper.getLastName();
    }

    public void setLastName(String lastName) {
        preferencesHelper.setLastName(lastName);
    }

    public String getEmail() {
        return preferencesHelper.getEmail();
    }

    public void setEmail(String email) {
        preferencesHelper.setEmail(email);
    }

    public void clearAllCaches() {
        preferencesHelper.clearAllCaches();
    }

    // Apis return
    public Observable<UserResponse> getUsers() {
        return apiService.getUsers();
    }

}
