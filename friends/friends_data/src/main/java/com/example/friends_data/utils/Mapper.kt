package com.example.friends_data.utils

import com.example.core.room.entitys.UserEntity
import com.example.friends_domain.models.UserModel

fun UserEntity.mapToUserModel() = UserModel(
    id = this.id,
    firstName = this.firstName,
    lastName = this.lastName,
    email = this.email,
    userPhoto = this.userPhoto
)