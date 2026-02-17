package com.example.app_data.repository

import com.example.app_data.remote.MainRemoteStorage
import com.example.app_data.utils.mapToEntity
import com.example.app_domain.repository.MainRepository
import com.example.core.UserSession
import com.example.core.room.dao.UserProfileDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val appScope: CoroutineScope,
    private val userSession: UserSession,
    private val mainRemoteStorage: MainRemoteStorage,
    private val userProfileDao: UserProfileDao
) : MainRepository {

    override suspend fun syncData() {
        if (mainRemoteStorage.isAuth()) {
            observeUserProfile()
        }
    }

    private fun observeUserProfile() {
        userSession.userId.filterNotNull().distinctUntilChanged().flatMapLatest { userId ->
            mainRemoteStorage.observeUserProfile(userId)
        }.onEach { remoteUserData ->
            if (!remoteUserData.id.isNullOrEmpty()) {
                val localUserData = userProfileDao.getUserById(remoteUserData.id!!).first()
                val entity = remoteUserData.mapToEntity()

                when {
                    localUserData == null -> {
                        userProfileDao.insertUser(entity)
                    }

                    localUserData != entity -> {
                        userProfileDao.updateUser(entity)
                    }

                    else -> Unit
                }
            }
        }.launchIn(appScope)
    }
}