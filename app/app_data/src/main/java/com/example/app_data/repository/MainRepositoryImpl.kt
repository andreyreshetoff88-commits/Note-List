package com.example.app_data.repository

import com.example.app_data.remote.MainRemoteStorage
import com.example.app_data.utils.SyncEvent
import com.example.app_data.utils.mapToEntity
import com.example.app_data.utils.mapToUserEntity
import com.example.app_domain.repository.MainRepository
import com.example.core.UserSession
import com.example.core.room.dao.UserDao
import com.example.core.room.dao.UserProfileDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val appScope: CoroutineScope,
    private val userSession: UserSession,
    private val mainRemoteStorage: MainRemoteStorage,
    private val userProfileDao: UserProfileDao,
    private val userDao: UserDao
) : MainRepository {

    override suspend fun syncData() {
        if (mainRemoteStorage.isAuth()) {
            observeUserProfile()
        }
    }

    private fun observeUserProfile() {
        userSession.userId.filterNotNull().distinctUntilChanged().flatMapLatest { userId ->
            merge(
                mainRemoteStorage.observeUserProfile(userId = userId),
                mainRemoteStorage.observeUsersFriends(userId = userId)
            )
        }.onEach { event ->
            when (event) {
                is SyncEvent.UserProfile -> syncUserProfile(event)
                is SyncEvent.FriendAdded -> syncFriendAdded(event)
                is SyncEvent.FriendDelete -> syncFriendDelete(event)
            }
        }.launchIn(appScope)
    }

    private suspend fun syncUserProfile(event: SyncEvent.UserProfile) {
        val remoteUserData = event.user
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

    private suspend fun syncFriendAdded(event: SyncEvent.FriendAdded) {
        val friendId = event.friendId
        if (!friendId.isNotEmpty()) {
            val friendUserData = mainRemoteStorage.getUserById(friendId)
            if (!friendUserData?.id.isNullOrEmpty()) {
                val localData = userDao.getUserById(
                    ownerUserId = friendId,
                    userId = event.userId
                ).first()
                val friendEntity = friendUserData.mapToUserEntity(event.userId)

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
    }

    private suspend fun syncFriendDelete(event: SyncEvent.FriendDelete) {
        val friendId = event.friendId
        if (friendId.isNotEmpty()) {
            userDao.deleteUserById(ownerUserId = event.userId, userId = friendId)
        }
    }
}