package com.example.data.di

import com.example.data.repository.AuthRepositoryImpl
import com.example.data.storage.AuthStorage
import com.example.data.storage.AuthStorageImpl
import com.example.domain.repository.AuthRepository
import com.example.domain.usecase.CheckVerifyUseCase
import com.example.domain.usecase.LoginUserUseCase
import com.example.domain.usecase.RegisterUserUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AuthModule {
    @Provides
    fun provideAuthStorage(
        firebaseAuth: FirebaseAuth,
        firebaseDatabase: DatabaseReference
    ): AuthStorage {
        return AuthStorageImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)
    }

    @Provides
    fun provideAuthRepository(authStorage: AuthStorage): AuthRepository {
        return AuthRepositoryImpl(authStorage = authStorage)
    }

    @Provides
    fun provideLoginUserUseCase(authRepository: AuthRepository) =
        LoginUserUseCase(authRepository = authRepository)

    @Provides
    fun provideRegisterUserUseCase(authRepository: AuthRepository) =
        RegisterUserUseCase(authRepository = authRepository)

    @Provides
    fun provideCheckVerifyEmailUseCase(authRepository: AuthRepository) =
        CheckVerifyUseCase(authRepository = authRepository)
}