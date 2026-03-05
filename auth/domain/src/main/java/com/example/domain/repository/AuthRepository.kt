package com.example.domain.repository

import com.example.domain.models.UserModel
import com.example.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun loginUser(email: String, password: String): Flow<Resource<Unit>>
    fun registerUser(userModel: UserModel, password: String): Flow<Resource<Unit>>
    fun checkVerifyEmail(): Boolean
}