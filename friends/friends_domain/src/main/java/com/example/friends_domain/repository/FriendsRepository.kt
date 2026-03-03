package com.example.friends_domain.repository

import com.example.friends_domain.models.UserModel
import com.example.friends_domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface FriendsRepository {
    suspend fun getUserFriends(): List<UserModel>
    fun observeUserFriends(): Flow<Resource<UserModel>>
}