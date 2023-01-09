package com.example.testapp.interfaces;

import com.example.testapp.models.UserResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface TestAppApiServices {
    @GET("api")
    Observable<UserResponse> getUsers();
}
