package com.example.profile_data.storage.local

import com.example.core.room.entitys.UserProfileEntity
import com.example.profile_data.models.GalleryImageDto
import kotlinx.coroutines.flow.Flow

interface ProfileLocalStorage {
    fun getUserInfo(): Flow<UserProfileEntity?>

    suspend fun getImages(): List<GalleryImageDto>
}