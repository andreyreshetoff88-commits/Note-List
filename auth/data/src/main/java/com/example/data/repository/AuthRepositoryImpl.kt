package com.example.data.repository

import com.example.data.storage.local.AuthLocalStorage
import com.example.data.storage.remote.AuthStorage
import com.example.data.utils.toDto
import com.example.data.utils.toEntity
import com.example.domain.models.UserModel
import com.example.domain.repository.AuthRepository
import com.example.domain.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authStorage: AuthStorage,
    private val authLocalStorage: AuthLocalStorage
) : AuthRepository {
    override fun loginUser(email: String, password: String) = flow {
        authStorage.loginUser(email = email, password = password).collect {
            when (it) {
                is Resource.Loading -> emit(Resource.Loading())
                is Resource.Success -> {
                    authLocalStorage.saveNewUserProfile(it.data!!.toEntity())
                    emit(Resource.Success(Unit))
                }

                is Resource.Error -> emit(Resource.Error(it.message))
            }
        }
    }

    override fun registerUser(userModel: UserModel, password: String) = flow {
        authStorage.registerUser(userModelDto = userModel.toDto(), password = password).collect {
            when (it) {
                is Resource.Loading -> emit(Resource.Loading())
                is Resource.Success -> {
                    authLocalStorage.saveNewUserProfile(it.data!!.toEntity())
                    emit(Resource.Success(Unit))
                }

                is Resource.Error -> emit(Resource.Error(it.message))
            }
        }
    }

    override fun checkVerifyEmail() = authStorage.checkVerifyEmail()
}