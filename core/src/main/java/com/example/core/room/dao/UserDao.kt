package com.example.core.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.core.room.entitys.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM users_friend WHERE ownerUserId = :ownerUserId AND id = :userId")
    fun getUserById(ownerUserId: String, userId: String): Flow<UserEntity?>

    @Query("SELECT * FROM users_friend WHERE ownerUserId = :ownerUserId")
    fun getUserByOwnerId(ownerUserId: String): Flow<UserEntity?>

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("DELETE FROM users_friend WHERE ownerUserId = :ownerUserId AND id = :userId")
    suspend fun deleteUserById(ownerUserId: String, userId: String)
}