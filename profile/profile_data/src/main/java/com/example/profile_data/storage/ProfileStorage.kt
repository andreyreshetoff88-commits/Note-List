package com.example.profile_data.storage

import com.example.profile_data.models.DataEditModelDto
import com.example.profile_domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ProfileStorage {
    suspend fun editFirstName(firstName: String): DataEditModelDto

    suspend fun editLastName(lastName: String): DataEditModelDto

    suspend fun signOut(): Flow<Resource<Boolean>>
}