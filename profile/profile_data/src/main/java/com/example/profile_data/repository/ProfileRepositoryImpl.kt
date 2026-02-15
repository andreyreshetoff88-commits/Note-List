package com.example.profile_data.repository

import android.util.Log
import com.example.core.Constants.USER_UID
import com.example.core.room.dao.UserDao
import com.example.profile_data.storage.ProfileStorage
import com.example.profile_data.utils.toDomain
import com.example.profile_domain.models.UserModel
import com.example.profile_domain.repository.ProfileRepository
import com.example.profile_domain.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val appScope: CoroutineScope,
    private val profileStorage: ProfileStorage,
    private val userDao: UserDao
) : ProfileRepository {
    override suspend fun getUserInfo() = flow {
        emit(Resource.Loading())
        try {
            userDao.getUserById(USER_UID).onEach { userEntity ->
                Log.d("ololo", "getUserInfo: $userEntity")
                if (userEntity != null) {
                    emit(
                        Resource.Success(
                            data = UserModel(
                                id = userEntity.id,
                                firstName = userEntity.firstName,
                                lastName = userEntity.lastName,
                                email = userEntity.email,
                                userPhoto = userEntity.userPhoto
                            )
                        )
                    )
                }
            }.launchIn(appScope)
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage))
        }
    }

    override suspend fun editFirstName(firstName: String) = flow {
        emit(Resource.Loading())
        val editModelDto = profileStorage.editFirstName(firstName = firstName)
        if (!editModelDto.error.isNullOrEmpty())
            emit(Resource.Error(message = editModelDto.error))
        else {
            emit(Resource.Success(data = editModelDto.toDomain()))
        }
    }

    override suspend fun editLastName(lastName: String) = flow {
        emit(Resource.Loading())
        val editModelDto = profileStorage.editLastName(lastName = lastName)
        if (!editModelDto.error.isNullOrEmpty())
            emit(Resource.Error(message = editModelDto.error))
        else {
            emit(Resource.Success(data = editModelDto.toDomain()))
        }
    }

    override suspend fun signOut() = profileStorage.signOut()
}