package com.example.profile_domain.usecase

import com.example.profile_domain.repository.ProfileRepository
import javax.inject.Inject

class EditFirstNameUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    suspend fun execute(firstName: String) = profileRepository.editFirstName(firstName = firstName)
}