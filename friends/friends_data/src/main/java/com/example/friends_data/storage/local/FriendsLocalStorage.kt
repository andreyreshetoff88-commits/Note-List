package com.example.friends_data.storage.local

import com.example.core.room.entitys.UserEntity
import com.example.friends_data.models.FriendDiff
import kotlinx.coroutines.flow.Flow

interface FriendsLocalStorage {
    suspend fun getUserFriends(): List<UserEntity>
    fun observeUserFriends(): Flow<FriendDiff>
}