package com.example.core.room.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_friend")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val ownerUserId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val userPhoto: String
)

