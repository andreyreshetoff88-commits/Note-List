package com.example.data.repository

import com.example.data.models.UserModelDto
import com.example.data.storage.AuthStorage
import com.example.data.utils.toDto
import com.example.domain.models.UserModel
import com.example.domain.repository.AuthRepository
import com.example.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authStorage: AuthStorage) :
    AuthRepository {
    override suspend fun loginUser(email: String, password: String) =
        authStorage.loginUser(email = email, password = password)

    override suspend fun registerUser(userModel: UserModel, password: String) =
        authStorage.registerUser(userModelDto = userModel.toDto(), password = password)
}