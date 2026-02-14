package com.example.data.storage

import com.example.data.models.UserModelDto
import com.example.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthStorage {
    suspend fun loginUser(email: String, password: String): Flow<Resource<Boolean>>

    suspend fun registerUser(userModelDto: UserModelDto, password: String): Flow<Resource<Boolean>>

    fun checkVerifyEmail(): Boolean
}