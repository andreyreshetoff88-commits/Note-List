package com.example.app_data.di

import com.example.app_data.remote.MainRemoteStorage
import com.example.app_data.remote.MainRemoteStorageImpl
import com.example.app_data.repository.MainRepositoryImpl
import com.example.app_domain.repository.MainRepository
import com.example.core.room.dao.UserDao
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
        firebaseDatabase: DatabaseReference
    ): MainRemoteStorage {
        return MainRemoteStorageImpl(
            firebaseAuth = firebaseAuth,
            firebaseDatabase = firebaseDatabase
        )
    }

    @Provides
    fun provideMainRepository(
        appScope: CoroutineScope,
        mainRemoteStorage: MainRemoteStorage,
        userDao: UserDao
    ): MainRepository {
        return MainRepositoryImpl(
            appScope = appScope,
            mainRemoteStorage = mainRemoteStorage,
            userDao = userDao
        )
    }
}