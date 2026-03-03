package com.example.friends_presentation.fragment.utils

import com.example.friends_domain.models.UserModel

sealed class FriendState(
    val data: UserModel? = null,
    val message: String? = null
) {
    class Empty : FriendState()
    class Loading : FriendState()
    class Added(data: UserModel) : FriendState(data = data)
    class Updated(data: UserModel) : FriendState(data = data)
    class Deleted(data: UserModel) : FriendState(data = data)
    class Error(message: String) : FriendState(message = message)
}