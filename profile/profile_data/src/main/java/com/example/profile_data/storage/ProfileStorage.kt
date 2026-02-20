package com.example.profile_data.storage

interface ProfileStorage {
    suspend fun editFirstName(firstName: String): Result<Unit>

    suspend fun editLastName(lastName: String): Result<Unit>

    suspend fun changePassword(oldPassword: String, newPassword: String): Result<Unit>

    suspend fun signOut(): Result<Unit>
}