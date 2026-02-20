package com.example.profile_domain.usecase

import com.example.profile_domain.repository.ProfileRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    suspend fun execute(oldPassword: String, newPassword: String) =
        profileRepository.changePassword(oldPassword = oldPassword, newPassword = newPassword)
}