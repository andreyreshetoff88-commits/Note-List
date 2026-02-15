package com.example.profile_domain.repository

import com.example.profile_domain.models.DataEditModel
import com.example.profile_domain.models.UserModel
import com.example.profile_domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun getUserInfo(): Flow<Resource<UserModel>>

    suspend fun editFirstName(firstName: String): Flow<Resource<DataEditModel>>

    suspend fun editLastName(lastName: String): Flow<Resource<DataEditModel>>

    suspend fun signOut(): Flow<Resource<Boolean>>
}