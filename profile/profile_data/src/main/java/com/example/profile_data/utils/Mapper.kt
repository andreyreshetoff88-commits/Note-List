package com.example.profile_data.utils

import androidx.core.net.toUri
import com.example.profile_data.models.DataEditModelDto
import com.example.profile_data.models.GalleryImageDto
import com.example.profile_domain.models.DataEditModel
import com.example.profile_domain.models.GalleryImage

fun DataEditModelDto.toDomain() = DataEditModel(
    success = this.success,
    error = this.error
)

fun GalleryImageDto.toDomain() = GalleryImage(
    id = this.id,
    uri = this.uri.toString()
)

fun GalleryImage.toData() = GalleryImageDto(
    id = this.id,
    uri = this.uri.toUri()
)