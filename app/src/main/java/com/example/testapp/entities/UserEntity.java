package com.example.testapp.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserEntity {
    @NonNull
    @PrimaryKey
    String userId;
}
