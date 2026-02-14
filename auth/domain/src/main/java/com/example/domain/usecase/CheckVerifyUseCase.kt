package com.example.domain.usecase

import com.example.domain.repository.AuthRepository
import javax.inject.Inject

class CheckVerifyUseCase @Inject constructor(private val authRepository: AuthRepository) {
    fun execute() = authRepository.checkVerifyEmail()
}