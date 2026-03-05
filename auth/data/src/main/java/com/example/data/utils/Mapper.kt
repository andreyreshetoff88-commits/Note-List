package com.example.data.utils

import com.example.core.room.entitys.UserProfileEntity
import com.example.data.models.UserModelDto
import com.example.domain.models.UserModel

fun UserModel.toDto() = UserModelDto(
    id = this.id,
    firstName = this.firstName,
    lastName = this.lastName,
    email = this.email,
    userPhoto = this.userPhoto
)

fun UserModelDto.toEntity() = UserProfileEntity(
    id = this.id!!,
    firstName = this.firstName,
    lastName = this.lastName,
    email = this.email,
    userPhoto = this.userPhoto
)