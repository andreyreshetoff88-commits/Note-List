package com.example.app_data.local

import com.example.app_data.models.UserModelDto
import com.example.app_data.utils.mapToEntity
import com.example.app_data.utils.mapToUserEntity
import com.example.core.room.dao.RequestDao
import com.example.core.room.dao.UserDao
import com.example.core.room.dao.UserProfileDao
import com.example.core.room.entitys.RequestEntity
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MainLocalStorageImpl @Inject constructor(
    private val userProfileDao: UserProfileDao,
    private val userDao: UserDao,
    private val requestDao: RequestDao
) : MainLocalStorage {
    override suspend fun syncUserProfile(remoteUserData: UserModelDto) {
        if (!remoteUserData.id.isNullOrEmpty()) {
            val localUserData = userProfileDao.getUserById(remoteUserData.id!!).first()
            val entity = remoteUserData.mapToEntity()

            when {
                localUserData == null -> {
                    userProfileDao.insertUser(entity)
                }

                localUserData != entity -> {
                    userProfileDao.updateUser(entity)
                }

                else -> Unit
            }
        }
    }

    override suspend fun syncFriendAdded(
        friendId: String,
        userId: String,
        friendUserData: UserModelDto?
    ) {
        if (!friendUserData?.id.isNullOrEmpty()) {
            val localData = userDao.getUserById(
                ownerUserId = friendId,
                userId = userId
            ).first()
            val friendEntity = friendUserData.mapToUserEntity(userId)

            when {
                localData == null -> {
                    userDao.insertUser(friendEntity)
                }

                localData != friendEntity -> {
                    userDao.updateUser(friendEntity)
                }

                else -> Unit
            }
        }
    }

    override suspend fun syncFriendDelete(friendId: String, userId: String) {
        userDao.deleteUserById(ownerUserId = userId, userId = friendId)
    }

    override suspend fun syncFriendRequestAdded(requestEntity: RequestEntity) {
        requestDao.insertRequest(requestEntity)
    }

    override suspend fun syncFriendRequestRemoved(recipientId: String, senderId: String) {
        requestDao.deleteRequest(recipientId = recipientId, senderId = senderId)
    }

    override suspend fun getRequestsOnce(recipientId: String) = requestDao.getRequestsOnce(recipientId)

    override suspend fun getUserFriendsIds(userId: String) =
        userDao.getUserFriendsIds(ownerUserId = userId)
}