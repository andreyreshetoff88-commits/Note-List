package com.example.verify_email_domain.repository

interface VerifyEmailRepository {
    fun sendVerifyEmail()

    fun checkVerifyEmail(): Boolean
}