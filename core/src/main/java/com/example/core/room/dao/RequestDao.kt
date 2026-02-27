package com.example.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.room.entitys.RequestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RequestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: RequestEntity)

    @Query("DELETE FROM users_requests WHERE recipientId = :recipientId AND senderId = :senderId")
    suspend fun deleteRequest(recipientId: String, senderId: String)

    @Query("SELECT * FROM users_requests WHERE recipientId = :recipientId")
    fun getRequestsForUser(recipientId: String): Flow<List<RequestEntity>>

    @Query("SELECT * FROM users_requests WHERE recipientId = :recipientId")
    fun getRequestsOnce(recipientId: String): List<RequestEntity>
}