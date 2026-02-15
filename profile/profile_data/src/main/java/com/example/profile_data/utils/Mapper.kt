package com.example.profile_data.utils

import com.example.profile_data.models.DataEditModelDto
import com.example.profile_domain.models.DataEditModel


fun DataEditModelDto.toDomain() = DataEditModel(
    success = this.success,
    error = this.error
)