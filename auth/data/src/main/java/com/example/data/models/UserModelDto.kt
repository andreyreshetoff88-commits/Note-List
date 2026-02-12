package com.example.data.models

data class UserModelDto(
    var id: String? = null,
    var firstName: String,
    val lastName: String,
    val email: String,
    var userPhoto: String = "null"
)
