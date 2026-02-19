package com.example.app_data.utils

import com.example.app_data.models.UserModelDto

sealed class SyncEvent {
    data class UserProfile(val user: UserModelDto) : SyncEvent()
    data class FriendAdded(val friendId: String, val userId: String) : SyncEvent()
    data class FriendDelete(val friendId: String, val userId: String) : SyncEvent()
}