package com.example.profile_data.utils

import com.example.profile_data.models.UserModelDto
import com.example.profile_domain.models.UserModel

fun UserModelDto.toDomain() = UserModel(
    id = this.id!!,
    firstName = this.firstName!!,
    lastName = this.lastName!!,
    email = this.email!!,
    userPhoto = this.userPhoto
)