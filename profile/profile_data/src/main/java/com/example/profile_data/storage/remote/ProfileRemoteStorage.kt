package com.example.profile_data.storage.remote

import android.net.Uri

interface ProfileRemoteStorage {
    suspend fun editFirstName(firstName: String): Result<Unit>

    suspend fun editLastName(lastName: String): Result<Unit>

    suspend fun changeUserPhoto(uri: Uri): Result<Unit>

    suspend fun changePassword(oldPassword: String, newPassword: String): Result<Unit>

    suspend fun signOut(): Result<Unit>
}