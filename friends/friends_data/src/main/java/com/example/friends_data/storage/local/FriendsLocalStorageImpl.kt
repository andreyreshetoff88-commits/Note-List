package com.example.friends_data.storage.local

import com.example.core.UserSession
import com.example.core.room.dao.UserDao
import com.example.friends_data.models.diff
import javax.inject.Inject

class FriendsLocalStorageImpl @Inject constructor(
    private val userSession: UserSession,
    private val userDao: UserDao
) : FriendsLocalStorage {
    override suspend fun getUserFriends() =
        userDao.getUserByOwnerIdOnce(ownerUserId = userSession.userId.value!!)

    override fun observeUserFriends() =
        userDao.getUserByOwnerId(ownerUserId = userSession.userId.value!!).diff()
}