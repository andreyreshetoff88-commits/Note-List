package com.example.app_data.remote

import com.example.app_data.models.UserModelDto
import kotlinx.coroutines.flow.Flow

interface MainRemoteStorage {
    fun isAuth(): Boolean

    fun observeUserProfile(): Flow<UserModelDto>

    fun observeUserFriends(): Flow<String>
}