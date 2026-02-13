package com.example.domain.repository

interface SplashRepository {
    fun checkUser(): Boolean

    fun checkVerifyEmail(): Boolean
}