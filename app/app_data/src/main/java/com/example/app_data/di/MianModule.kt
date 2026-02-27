package com.example.app_data.di

import com.example.app_data.local.MainLocalStorage
import com.example.app_data.local.MainLocalStorageImpl
import com.example.app_data.remote.MainRemoteStorage
import com.example.app_data.remote.MainRemoteStorageImpl
import com.example.app_data.repository.MainRepositoryImpl
import com.example.app_domain.repository.MainRepository
import com.example.core.UserSession
import com.example.core.room.dao.RequestDao
import com.example.core.room.dao.UserDao
import com.example.core.room.dao.UserProfileDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(SingletonComponent::class)
class MianModule {
    @Provides
    fun provideMainRemoteStorage(
        firebaseAuth: FirebaseAuth,
        firebaseDatabase: DatabaseReference,
        userSession: UserSession
    ): MainRemoteStorage {
        return MainRemoteStorageImpl(
            firebaseAuth = firebaseAuth,
            firebaseDatabase = firebaseDatabase,
            userSession = userSession
        )
    }

    @Provides
    fun provideMainLocalStorage(
        userProfileDao: UserProfileDao,
        userDao: UserDao,
        requestDao: RequestDao
    ): MainLocalStorage {
        return MainLocalStorageImpl(
            userProfileDao = userProfileDao,
            userDao = userDao,
            requestDao = requestDao
        )
    }

    @Provides
    fun provideMainRepository(
        appScope: CoroutineScope,
        userSession: UserSession,
        mainRemoteStorage: MainRemoteStorage,
        mainLocalStorage: MainLocalStorage
    ): MainRepository {
        return MainRepositoryImpl(
            appScope = appScope,
            userSession = userSession,
            mainRemoteStorage = mainRemoteStorage,
            mainLocalStorage = mainLocalStorage
        )
    }
}