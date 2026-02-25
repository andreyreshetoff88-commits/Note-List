package com.example.profile_data.di

import android.content.Context
import com.example.core.cloudinary.CloudinaryUploader
import com.example.core.UserSession
import com.example.core.room.dao.UserProfileDao
import com.example.profile_data.repository.ProfileRepositoryImpl
import com.example.profile_data.storage.local.ProfileLocalStorage
import com.example.profile_data.storage.local.ProfileLocalStorageImpl
import com.example.profile_data.storage.remote.ProfileRemoteStorage
import com.example.profile_data.storage.remote.ProfileRemoteStorageImpl
import com.example.profile_domain.repository.ProfileRepository
import com.example.profile_domain.usecase.ChangePasswordUseCase
import com.example.profile_domain.usecase.ChangeUserPhotoUseCase
import com.example.profile_domain.usecase.EditFirstNameUseCase
import com.example.profile_domain.usecase.EditLastNameUseCase
import com.example.profile_domain.usecase.GetImagesUseCase
import com.example.profile_domain.usecase.GetUserInfoUseCase
import com.example.profile_domain.usecase.SignOutUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ProfileModule {
    @Provides
    fun provideProfileRemoteStorage(
        firebaseAuth: FirebaseAuth,
        firebaseDatabase: DatabaseReference,
        userSession: UserSession,
        cloudinaryUploader: CloudinaryUploader
    ): ProfileRemoteStorage {
        return ProfileRemoteStorageImpl(
            firebaseAuth = firebaseAuth,
            firebaseDatabase = firebaseDatabase,
            userSession = userSession,
            cloudinaryUploader = cloudinaryUploader
        )
    }

    @Provides
    fun provideProfileLocalStorage(
        @ApplicationContext context: Context,
        userProfileDao: UserProfileDao,
        userSession: UserSession
    ): ProfileLocalStorage {
        return ProfileLocalStorageImpl(
            context = context,
            userProfileDao = userProfileDao,
            userSession = userSession
        )
    }

    @Provides
    fun provideProfileRepository(
        profileRemoteStorage: ProfileRemoteStorage,
        profileLocalStorage: ProfileLocalStorage
    ): ProfileRepository {
        return ProfileRepositoryImpl(
            profileRemoteStorage = profileRemoteStorage,
            profileLocalStorage = profileLocalStorage
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

    @Provides
    fun provideChangePasswordUseCase(profileRepository: ProfileRepository) =
        ChangePasswordUseCase(profileRepository = profileRepository)

    @Provides
    fun provideGetImagesUseCase(profileRepository: ProfileRepository) =
        GetImagesUseCase(profileRepository = profileRepository)

    @Provides
    fun provideChangeUserPhotoUseCase(profileRepository: ProfileRepository) =
        ChangeUserPhotoUseCase(profileRepository = profileRepository)
}