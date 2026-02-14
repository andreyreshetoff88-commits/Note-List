package com.example.core.room.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val userId: String,
    val groupId: String,
    val date: Long,
    val completed: Boolean,
    val read: Boolean
)

