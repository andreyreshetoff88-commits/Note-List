package com.example.app_data.utils

import com.example.app_data.models.UserModelDto

sealed class SyncEvent {
    data class UserProfile(val user: UserModelDto) : SyncEvent()
    data class FriendAdded(val friendId: String, val userId: String) : SyncEvent()
    data class FriendDelete(val friendId: String, val userId: String) : SyncEvent()
    data class FriendRequestAdded(val recipientId: String, val senderId: String) : SyncEvent()
    data class FriendRequestRemoved(val recipientId: String, val senderId: String) : SyncEvent()
}