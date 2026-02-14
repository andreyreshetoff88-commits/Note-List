package com.example.profile_data.storage

import com.example.profile_data.models.UserModelDto
import com.example.profile_domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ProfileStorage {
    suspend fun getUserInfo(): UserModelDto

    suspend fun editFirstName(firstName: String): UserModelDto

    suspend fun editLastName(lastName: String): UserModelDto

    suspend fun signOut(): Flow<Resource<Boolean>>
}