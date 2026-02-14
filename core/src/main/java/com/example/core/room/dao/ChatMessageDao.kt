package com.example.core.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.core.room.entitys.ChatMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(chatMessage: ChatMessageEntity)

    @Update
    suspend fun updateGroup(chatMessage: ChatMessageEntity)

    @Query("SELECT * FROM chat_messages WHERE id = :chatMessageId")
    fun getGroupById(chatMessageId: String): Flow<ChatMessageEntity?>

    @Delete
    suspend fun deleteGroup(chatMessage: ChatMessageEntity)
}