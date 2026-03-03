package com.example.friends_data.di

import com.example.core.UserSession
import com.example.core.room.dao.UserDao
import com.example.friends_data.repository.FriendsRepositoryImpl
import com.example.friends_data.storage.local.FriendsLocalStorage
import com.example.friends_data.storage.local.FriendsLocalStorageImpl
import com.example.friends_domain.repository.FriendsRepository
import com.example.friends_domain.usecase.GetUserFriendsUseCase
import com.example.friends_domain.usecase.ObserveUserFriendsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class FriendsModule {
    @Provides
    fun provideFriendsLocalStorage(
        userSession: UserSession,
        userDao: UserDao
    ): FriendsLocalStorage {
        return FriendsLocalStorageImpl(
            userSession = userSession,
            userDao = userDao
        )
    }

    @Provides
    fun provideFriendsRepository(
        friendsLocalStorage: FriendsLocalStorage
    ): FriendsRepository {
        return FriendsRepositoryImpl(
            friendsLocalStorage = friendsLocalStorage
        )
    }

    @Provides
    fun provideGetUserFriendsUseCase(friendsRepository: FriendsRepository) =
        GetUserFriendsUseCase(friendsRepository = friendsRepository)

    @Provides
    fun provideObserveUserFriendsUseCase(friendsRepository: FriendsRepository) =
        ObserveUserFriendsUseCase(friendsRepository = friendsRepository)
}