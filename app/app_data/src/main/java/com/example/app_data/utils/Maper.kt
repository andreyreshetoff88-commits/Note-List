package com.example.app_data.utils

import com.example.app_data.models.UserModelDto
import com.example.core.room.entitys.UserEntity
import com.example.core.room.entitys.UserProfileEntity

fun UserModelDto.mapToEntity() = UserProfileEntity(
    id = this.id!!,
    firstName = this.firstName!!,
    lastName = this.lastName!!,
    email = this.email!!,
    userPhoto = this.userPhoto!!
)

fun UserModelDto.mapToUserEntity(ownerId: String) = UserEntity(
    id = this.id!!,
    ownerUserId = ownerId,
    firstName = this.firstName!!,
    lastName = this.lastName!!,
    email = this.email!!,
    userPhoto = this.userPhoto!!
)