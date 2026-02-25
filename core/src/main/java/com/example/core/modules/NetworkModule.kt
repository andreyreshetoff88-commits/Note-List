package com.example.core.modules

import com.example.core.cloudinary.CloudinaryUploader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseDatabase() = FirebaseDatabase.getInstance().reference

    @Singleton
    @Provides
    fun provideCloudinaryUploader() = CloudinaryUploader()
}