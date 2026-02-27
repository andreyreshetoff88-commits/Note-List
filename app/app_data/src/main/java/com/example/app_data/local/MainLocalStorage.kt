package com.example.app_data.local

import com.example.app_data.models.UserModelDto
import com.example.core.room.entitys.RequestEntity

interface MainLocalStorage {
    suspend fun syncUserProfile(remoteUserData: UserModelDto)
    suspend fun syncFriendAdded(friendId: String, userId: String, friendUserData: UserModelDto?)
    suspend fun syncFriendDelete(friendId: String, userId: String)
    suspend fun syncFriendRequestAdded(requestEntity: RequestEntity)
    suspend fun syncFriendRequestRemoved(recipientId: String, senderId: String)
    suspend fun getRequestsOnce(recipientId: String): List<RequestEntity>
    suspend fun getUserFriendsIds(userId: String): List<String>
}