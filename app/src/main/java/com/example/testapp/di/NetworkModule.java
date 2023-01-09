package com.example.testapp.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.testapp.commons.CommonKeys;
import com.example.testapp.configs.PreferencesHelper;
import com.example.testapp.interfaces.TestAppApiServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(ApplicationComponent.class)
public class NetworkModule {

    @Provides
    @Singleton
    public SharedPreferences providesSharedPreferences(Application application) {
        return application.getSharedPreferences(CommonKeys.APP_NAME, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    public PreferencesHelper providesPreferencesHelper(SharedPreferences sharedPreferences) {
        return new PreferencesHelper(sharedPreferences);
    }

    @Provides
    @Singleton
    public static TestAppApiServices provideTestAppApiService() {

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(CommonKeys.baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
                .create(TestAppApiServices.class);
    }
}

