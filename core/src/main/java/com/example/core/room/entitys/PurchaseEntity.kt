package com.example.core.room.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "purchases")
data class PurchaseEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val userId: String,
    val groupId: String,
    val date: Long,
    val bought: Boolean,
    val read: Boolean
)

