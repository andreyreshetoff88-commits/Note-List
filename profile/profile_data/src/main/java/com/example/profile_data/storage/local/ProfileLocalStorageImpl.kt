package com.example.profile_data.storage.local

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.example.core.UserSession
import com.example.core.room.dao.UserProfileDao
import com.example.profile_data.models.GalleryImageDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileLocalStorageImpl @Inject constructor(
    private val context: Context,
    private val userProfileDao: UserProfileDao,
    private val userSession: UserSession
) : ProfileLocalStorage {
    override fun getUserInfo() = userProfileDao.getUserById(userSession.userId.value!!)

    override suspend fun getImages(): List<GalleryImageDto> = withContext(Dispatchers.IO) {
        val images = mutableListOf<GalleryImageDto>()
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        val queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        context.contentResolver.query(
            queryUri,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val uri = ContentUris.withAppendedId(queryUri, id)
                images.add(GalleryImageDto(id, uri))
            }
        }

        images
    }
}