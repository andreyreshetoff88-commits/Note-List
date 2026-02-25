package com.example.profile_domain.usecase

import com.example.profile_domain.repository.ProfileRepository
import javax.inject.Inject

class ChangeUserPhotoUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    suspend fun execute(uri: String) =
        profileRepository.changeUserPhoto(uri = uri)
}