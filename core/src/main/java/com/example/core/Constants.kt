package com.example.core

object Constants {
    val PASSWORD_REGEX =
        Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[_-])[A-Za-z\\d_-]{8,}$")

    const val NODE_USERS = "users"
    const val NODE_GROUPS = "groups"
    const val NODE_USERS_FRIENDS = "usersFriends"
    const val NODE_FRIEND_REQUESTS = "FriendRequests"
    const val NODE_USERS_GROUPS = "usersGroups"
}