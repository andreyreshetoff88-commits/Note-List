package com.example.data.di

import com.example.data.repository.SplashRepositoryIml
import com.example.domain.repository.SplashRepository
import com.example.domain.usecase.CheckUserUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class SplashModule {
    @Provides
    fun provideSplashRepository(firebaseAuth: FirebaseAuth): SplashRepository {
        return SplashRepositoryIml(firebaseAuth = firebaseAuth)
    }

    @Provides
    fun provideCheckUserUseCase(splashRepository: SplashRepository) =
        CheckUserUseCase(splashRepository = splashRepository)
}