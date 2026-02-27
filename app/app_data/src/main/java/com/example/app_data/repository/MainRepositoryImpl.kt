package com.example.app_data.repository

import com.example.app_data.local.MainLocalStorage
import com.example.app_data.remote.MainRemoteStorage
import com.example.app_data.utils.SyncEvent
import com.example.app_domain.repository.MainRepository
import com.example.core.UserSession
import com.example.core.room.entitys.RequestEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val appScope: CoroutineScope,
    private val userSession: UserSession,
    private val mainRemoteStorage: MainRemoteStorage,
    private val mainLocalStorage: MainLocalStorage
) : MainRepository {

    override suspend fun syncData() {
        if (mainRemoteStorage.isAuth()) {
            observeUserProfile()
        }
    }

    private fun observeUserProfile() {
        userSession.userId.filterNotNull().distinctUntilChanged()
            .onEach { userId ->
                syncFriendRequestsInitial(userId = userId)
                syncFriendsInitial(userId = userId)
            }
            .flatMapLatest { userId ->
                merge(
                    mainRemoteStorage.observeUserProfile(userId = userId),
                    mainRemoteStorage.observeUsersFriends(userId = userId),
                    mainRemoteStorage.observeFriendRequests(userId = userId)
                )
            }.onEach { event ->
                when (event) {
                    is SyncEvent.UserProfile ->
                        mainLocalStorage.syncUserProfile(remoteUserData = event.user)

                    is SyncEvent.FriendAdded -> mainLocalStorage.syncFriendAdded(
                        friendId = event.friendId,
                        userId = event.userId,
                        friendUserData = mainRemoteStorage.getUserById(userId = event.friendId)
                    )

                    is SyncEvent.FriendDelete -> mainLocalStorage.syncFriendDelete(
                        friendId = event.friendId,
                        userId = event.userId
                    )

                    is SyncEvent.FriendRequestAdded -> mainLocalStorage.syncFriendRequestAdded(
                        RequestEntity(recipientId = event.recipientId, senderId = event.senderId)
                    )

                    is SyncEvent.FriendRequestRemoved -> mainLocalStorage.syncFriendRequestRemoved(
                        recipientId = event.recipientId, senderId = event.senderId
                    )
                }
            }.launchIn(appScope)
    }

    private suspend fun syncFriendRequestsInitial(userId: String) {
        val remoteRequestIds = mainRemoteStorage.getAllFriendRequests(userId = userId)
        val localRequests = mainLocalStorage.getRequestsOnce(recipientId = userId)

        val localSenderIds = localRequests.map { it.senderId }

        localSenderIds.filter { it !in remoteRequestIds }.forEach { senderId ->
            mainLocalStorage.syncFriendRequestRemoved(recipientId = userId, senderId = senderId)
        }

        remoteRequestIds.filter { it !in localSenderIds }.forEach { senderId ->
            mainLocalStorage.syncFriendRequestAdded(
                RequestEntity(
                    recipientId = userId,
                    senderId = senderId
                )
            )
        }
    }

    private suspend fun syncFriendsInitial(userId: String) {
        val remoteFriendsIds = mainRemoteStorage.getUserFriendsIds(userId = userId)
        val localFriendsIds = mainLocalStorage.getUserFriendsIds(userId = userId)

        localFriendsIds.filter { it !in remoteFriendsIds }.forEach { friendId ->
            mainLocalStorage.syncFriendDelete(friendId = friendId, userId = userId)
        }

        remoteFriendsIds.filter { it !in localFriendsIds }.forEach { friendId ->
            mainLocalStorage.syncFriendAdded(
                friendId = friendId,
                userId = userId,
                friendUserData = mainRemoteStorage.getUserById(userId = friendId)
            )
        }
    }
}