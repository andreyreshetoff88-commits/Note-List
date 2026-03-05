package com.example.data.storage.local

import com.example.core.room.dao.UserProfileDao
import com.example.core.room.entitys.UserProfileEntity
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthLocalStorageImpl @Inject constructor(
    private val userProfileDao: UserProfileDao
) : AuthLocalStorage {
    override suspend fun saveNewUserProfile(entity: UserProfileEntity) {
        val userProfileEntity = userProfileDao.getUserById(entity.id).first()

        when {
            userProfileEntity == null -> {
                userProfileDao.insertUser(entity)
            }

            userProfileEntity != entity -> {
                userProfileDao.updateUser(entity)
            }

            else -> Unit
        }
    }
}