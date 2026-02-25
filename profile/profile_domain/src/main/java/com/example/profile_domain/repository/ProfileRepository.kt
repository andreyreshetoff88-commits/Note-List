package com.example.profile_domain.repository

import com.example.profile_domain.models.GalleryImage
import com.example.profile_domain.models.UserModel
import com.example.profile_domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getUserInfo(): Flow<Resource<UserModel>>

    suspend fun editFirstName(firstName: String): Flow<Resource<Unit>>

    suspend fun editLastName(lastName: String): Flow<Resource<Unit>>

    suspend fun changeUserPhoto(uri: String): Flow<Resource<Unit>>

    suspend fun changePassword(oldPassword: String, newPassword: String): Flow<Resource<Unit>>

    suspend fun getImages(): List<GalleryImage>

    suspend fun signOut(): Flow<Resource<Unit>>
}