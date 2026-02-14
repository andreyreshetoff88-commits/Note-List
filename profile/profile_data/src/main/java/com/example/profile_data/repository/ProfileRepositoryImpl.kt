package com.example.profile_data.repository

import com.example.profile_data.storage.ProfileStorage
import com.example.profile_data.utils.toDomain
import com.example.profile_domain.repository.ProfileRepository
import com.example.profile_domain.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(private val profileStorage: ProfileStorage) :
    ProfileRepository {
    override suspend fun getUserInfo() = flow {
        emit(Resource.Loading())
        val userModelDto = profileStorage.getUserInfo()
        if (!userModelDto.error.isNullOrEmpty())
            emit(Resource.Error(message = userModelDto.error))
        else {
            val userModel = userModelDto.toDomain()
            emit(Resource.Success(data = userModel))
        }
    }

    override suspend fun editFirstName(firstName: String) = flow {
        emit(Resource.Loading())
        val userModelDto = profileStorage.editFirstName(firstName = firstName)
        if (!userModelDto.error.isNullOrEmpty())
            emit(Resource.Error(message = userModelDto.error))
        else {
            val userModel = userModelDto.toDomain()
            emit(Resource.Success(data = userModel))
        }
    }

    override suspend fun editLastName(lastName: String) = flow {
        emit(Resource.Loading())
        val userModelDto = profileStorage.editLastName(lastName = lastName)
        if (!userModelDto.error.isNullOrEmpty())
            emit(Resource.Error(message = userModelDto.error))
        else {
            val userModel = userModelDto.toDomain()
            emit(Resource.Success(data = userModel))
        }
    }

    override suspend fun signOut() = profileStorage.signOut()
}