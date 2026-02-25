package com.example.profile_data.repository

import androidx.core.net.toUri
import com.example.profile_data.storage.local.ProfileLocalStorage
import com.example.profile_data.storage.remote.ProfileRemoteStorage
import com.example.profile_data.utils.toDomain
import com.example.profile_domain.models.GalleryImage
import com.example.profile_domain.models.UserModel
import com.example.profile_domain.repository.ProfileRepository
import com.example.profile_domain.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileRemoteStorage: ProfileRemoteStorage,
    private val profileLocalStorage: ProfileLocalStorage
) : ProfileRepository {
    override fun getUserInfo() =
        profileLocalStorage.getUserInfo().map { entity ->
            if (entity == null)
                Resource.Error(message = "Пользователь не найден")
            else
                Resource.Success(
                    UserModel(
                        id = entity.id,
                        firstName = entity.firstName,
                        lastName = entity.lastName,
                        email = entity.email,
                        userPhoto = entity.userPhoto
                    )
                )
        }.onStart {
            emit(Resource.Loading())
        }.catch { e ->
            emit(Resource.Error(e.message))
        }


    override suspend fun editFirstName(firstName: String) =
        handleResult(profileRemoteStorage.editFirstName(firstName = firstName))

    override suspend fun editLastName(lastName: String) =
        handleResult(profileRemoteStorage.editLastName(lastName = lastName))

    override suspend fun changeUserPhoto(uri: String) =
        handleResult(profileRemoteStorage.changeUserPhoto(uri = uri.toUri()))

    override suspend fun changePassword(oldPassword: String, newPassword: String) =
        handleResult(
            profileRemoteStorage.changePassword(
                oldPassword = oldPassword,
                newPassword = newPassword
            )
        )

    override suspend fun signOut() = handleResult(profileRemoteStorage.signOut())

    override suspend fun getImages(): List<GalleryImage> {
        val userImages = mutableListOf<GalleryImage>()

        profileLocalStorage.getImages().forEach { image ->
            userImages.add(image.toDomain())
        }

        return userImages
    }

    private fun <T> handleResult(result: Result<T>) = flow {
        emit(Resource.Loading())
        result.onSuccess { emit(Resource.Success(Unit)) }
            .onFailure { emit(Resource.Error(it.message)) }
    }
}