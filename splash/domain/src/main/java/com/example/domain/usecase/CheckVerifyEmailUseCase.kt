package com.example.domain.usecase

import com.example.domain.repository.SplashRepository
import javax.inject.Inject

class CheckVerifyEmailUseCase @Inject constructor(private val splashRepository: SplashRepository) {
    fun execute() = splashRepository.checkVerifyEmail()
}