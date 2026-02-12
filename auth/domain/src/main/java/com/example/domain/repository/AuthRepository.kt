package com.example.domain.repository

import com.example.domain.models.UserModel
import com.example.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun loginUser(email: String, password: String): Flow<Resource<Boolean>>

    suspend fun registerUser(userModel: UserModel, password: String): Flow<Resource<Boolean>>
}