package com.example.app_data.remote

import com.example.app_data.models.UserModelDto
import kotlinx.coroutines.flow.Flow

interface MainRemoteStorage {
    suspend fun isAuth(): Boolean

    fun observeUserProfile(userId: String): Flow<UserModelDto>

    fun observeUserFriends(userId: String): Flow<String>
}