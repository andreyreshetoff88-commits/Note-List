package com.example.data.storage.remote

import com.example.data.models.UserModelDto
import com.example.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthStorage {
    fun loginUser(email: String, password: String): Flow<Resource<UserModelDto>>
    fun registerUser(userModelDto: UserModelDto, password: String): Flow<Resource<UserModelDto>>
    suspend fun getUser(): Result<UserModelDto>
    fun checkVerifyEmail(): Boolean
}