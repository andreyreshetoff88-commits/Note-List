package com.example.profile_domain.models

data class UserModel(
    var id: String,
    var firstName: String,
    var lastName: String,
    var email: String,
    var userPhoto: String? = null,
    var error: String? = null
)
