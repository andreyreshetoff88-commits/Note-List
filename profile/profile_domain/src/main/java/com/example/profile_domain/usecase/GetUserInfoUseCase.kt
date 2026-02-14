package com.example.profile_domain.usecase

import com.example.profile_domain.repository.ProfileRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    suspend fun execute() = profileRepository.getUserInfo()
}