package com.example.core.room.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey
    val id: String,
    val text: String,
    val userId: String,
    val groupId: String,
    val date: Long,
    val read: Boolean
)
