package com.example.domain.usecase

import com.example.domain.models.UserModel
import com.example.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend fun execute(userModel: UserModel, password: String) =
        authRepository.registerUser(userModel = userModel, password = password)
}