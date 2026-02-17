package com.example.profile_data.di

import com.example.core.UserSession
import com.example.core.room.dao.UserProfileDao
import com.example.profile_data.repository.ProfileRepositoryImpl
import com.example.profile_data.storage.ProfileStorage
import com.example.profile_data.storage.ProfileStorageImpl
import com.example.profile_domain.repository.ProfileRepository
import com.example.profile_domain.usecase.EditFirstNameUseCase
import com.example.profile_domain.usecase.EditLastNameUseCase
import com.example.profile_domain.usecase.GetUserInfoUseCase
import com.example.profile_domain.usecase.SignOutUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ProfileModule {
    @Provides
    fun provideProfileStorage(
        firebaseAuth: FirebaseAuth,
        firebaseDatabase: DatabaseReference,
        userSession: UserSession
    ): ProfileStorage {
        return ProfileStorageImpl(
            firebaseAuth = firebaseAuth,
            firebaseDatabase = firebaseDatabase,
            userSession = userSession
        )
    }

    @Provides
    fun provideProfileRepository(
        profileStorage: ProfileStorage,
        userProfileDao: UserProfileDao,
        userSession: UserSession
    ): ProfileRepository {
        return ProfileRepositoryImpl(
            profileStorage = profileStorage,
            userProfileDao = userProfileDao,
            userSession = userSession
        )
    }

    @Provides
    fun provideSignOutUseCase(profileRepository: ProfileRepository) =
        SignOutUseCase(profileRepository = profileRepository)

    @Provides
    fun provideGetUserInfoUseCase(profileRepository: ProfileRepository) =
        GetUserInfoUseCase(profileRepository = profileRepository)

    @Provides
    fun provideEditFirstNameUseCase(profileRepository: ProfileRepository) =
        EditFirstNameUseCase(profileRepository = profileRepository)

    @Provides
    fun provideEditLastNameUseCase(profileRepository: ProfileRepository) =
        EditLastNameUseCase(profileRepository = profileRepository)
}