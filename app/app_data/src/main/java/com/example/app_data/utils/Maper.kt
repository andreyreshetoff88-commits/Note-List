package com.example.app_data.utils

import com.example.app_data.models.UserModelDto
import com.example.core.room.entitys.UserEntity

fun UserModelDto.mapToEntity() = UserEntity(
    id = this.id!!,
    firstName = this.firstName!!,
    lastName = this.lastName!!,
    email = this.email!!,
    userPhoto = this.userPhoto!!
)