package com.example.data.storage.local

import com.example.core.room.entitys.UserProfileEntity

interface AuthLocalStorage {
    suspend fun saveNewUserProfile(entity: UserProfileEntity)
}