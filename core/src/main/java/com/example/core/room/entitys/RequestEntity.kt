package com.example.core.room.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_requests")
data class RequestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val recipientId: String,
    val senderId: String
)