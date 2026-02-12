package com.example.domain.usecase

import com.example.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend fun execute(email: String, password: String) =
        authRepository.loginUser(email = email, password = password)
}