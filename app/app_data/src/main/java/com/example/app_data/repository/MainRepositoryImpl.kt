package com.example.app_data.repository

import com.example.app_data.remote.MainRemoteStorage
import com.example.app_data.utils.mapToEntity
import com.example.app_domain.repository.MainRepository
import com.example.core.Constants.USER_UID
import com.example.core.room.dao.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val appScope: CoroutineScope,
    private val mainRemoteStorage: MainRemoteStorage,
    private val userDao: UserDao
) : MainRepository {
    override suspend fun syncData() {
        if (mainRemoteStorage.isAuth()) {
            observeUserProfile()
        }
    }

    private fun observeUserProfile() {
        mainRemoteStorage.observeUserProfile().onEach { remoteUserData ->
            val localUserData = userDao.getUserById(USER_UID).first()
            when {
                localUserData == null -> userDao.insertUser(remoteUserData.mapToEntity())

                localUserData != remoteUserData.mapToEntity() ->
                    userDao.updateUser(remoteUserData.mapToEntity())

                else -> Unit
            }
        }.launchIn(appScope)
    }
}