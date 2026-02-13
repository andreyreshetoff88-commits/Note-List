package com.example.verify_email_data.di

import com.example.verify_email_data.repository.VerifyEmailRepositoryImpl
import com.example.verify_email_domain.repository.VerifyEmailRepository
import com.example.verify_email_domain.usecase.CheckVerifyEmailUseCase
import com.example.verify_email_domain.usecase.SendVerifyEmailUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class VerifyEmailModule {
    @Provides
    fun provideVerifyEmailRepository(firebaseAuth: FirebaseAuth): VerifyEmailRepository {
        return VerifyEmailRepositoryImpl(firebaseAuth = firebaseAuth)
    }

    @Provides
    fun provideSendVerifyEmailUseCase(verifyEmailRepository: VerifyEmailRepository) =
        SendVerifyEmailUseCase(verifyEmailRepository = verifyEmailRepository)

    @Provides
    fun provideCheckVerifyEmailUseCase(verifyEmailRepository: VerifyEmailRepository) =
        CheckVerifyEmailUseCase(verifyEmailRepository = verifyEmailRepository)
}