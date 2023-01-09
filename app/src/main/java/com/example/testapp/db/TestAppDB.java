package com.example.testapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.testapp.dao.TestAppDao;
import com.example.testapp.entities.UserEntity;

@Database(entities = {UserEntity.class},version = 1,exportSchema = false)
public abstract class TestAppDB extends RoomDatabase {
    public abstract TestAppDao testAppDao();
}
