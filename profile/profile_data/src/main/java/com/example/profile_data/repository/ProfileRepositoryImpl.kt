package com.example.profile_data.repository

import com.example.core.UserSession
import com.example.core.room.dao.UserProfileDao
import com.example.profile_data.storage.ProfileStorage
import com.example.profile_domain.models.UserModel
import com.example.profile_domain.repository.ProfileRepository
import com.example.profile_domain.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileStorage: ProfileStorage,
    private val userProfileDao: UserProfileDao,
    private val userSession: UserSession
) : ProfileRepository {
    override fun getUserInfo() =
        userProfileDao.getUserById(userSession.userId.value!!).map { entity ->
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
        handleResult(profileStorage.editFirstName(firstName = firstName))

    override suspend fun editLastName(lastName: String) =
        handleResult(profileStorage.editLastName(lastName = lastName))

    override suspend fun changePassword(oldPassword: String, newPassword: String) =
        handleResult(
            profileStorage.changePassword(
                oldPassword = oldPassword,
                newPassword = newPassword
            )
        )

    override suspend fun signOut() = handleResult(profileStorage.signOut())

    private fun <T> handleResult(result: Result<T>) = flow {
        emit(Resource.Loading())
        result.onSuccess { emit(Resource.Success(Unit)) }
            .onFailure { emit(Resource.Error(it.message)) }
    }
}