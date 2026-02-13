package com.example.verify_email_domain.usecase

import com.example.verify_email_domain.repository.VerifyEmailRepository
import javax.inject.Inject

class SendVerifyEmailUseCase @Inject constructor(private val verifyEmailRepository: VerifyEmailRepository) {
    fun execute() = verifyEmailRepository.sendVerifyEmail()
}