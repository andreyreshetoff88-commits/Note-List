package com.example.core

object Constants {
    val PASSWORD_REGEX =
        Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[_-])[A-Za-z\\d_-]{8,}$")
    var USER_UID = ""

    val USERS = mutableListOf<String>()

    const val NODE_USERS = "users"
    const val NODE_USER_FRIENDS = "userFriends"
    const val NODE_GROUPS = "groups"

    const val CHILD_USER_GROUPS = "userGroups"
}