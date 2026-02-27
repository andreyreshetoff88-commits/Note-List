package com.example.app_data.remote

import com.example.app_data.models.UserModelDto
import com.example.app_data.utils.SyncEvent
import kotlinx.coroutines.flow.Flow

interface MainRemoteStorage {
    suspend fun isAuth(): Boolean
    fun observeUserProfile(userId: String): Flow<SyncEvent>
    fun observeUsersFriends(userId: String): Flow<SyncEvent>
    suspend fun getUserById(userId: String): UserModelDto?
    suspend fun observeFriendRequests(userId: String): Flow<SyncEvent>
    suspend fun getAllUserFriendsIds(userId: String): List<String>
    suspend fun getAllFriendRequests(userId: String): List<String>
    suspend fun getUserFriendsIds(userId: String): List<String>
}