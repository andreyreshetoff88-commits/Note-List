package com.example.core.room.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "group_table")
data class GroupEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val createdAt: Long,
    val lastChange: Long,
    val groupPhoto: String
)

