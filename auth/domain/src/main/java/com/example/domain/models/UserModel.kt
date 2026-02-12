package com.example.domain.models

data class UserModel(
    var id: String? = null,
    var firstName: String,
    val lastName: String,
    val email: String,
    var userPhoto: String = "null"
)
