package com.example.testapp.di;

import android.app.Application;

import androidx.room.Room;

import com.example.testapp.dao.TestAppDao;
import com.example.testapp.db.TestAppDB;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

@Module
@InstallIn(ApplicationComponent.class)
public class DatabaseModule {

    @Provides
    @Singleton
    public static TestAppDB provideTestAppDB(Application application){
        return Room.databaseBuilder(application,TestAppDB.class,"TestApp Database")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    @Provides
    @Singleton
    public static TestAppDao provideTestAppDao(TestAppDB testAppDB){
        return testAppDB.testAppDao();
    }
}
