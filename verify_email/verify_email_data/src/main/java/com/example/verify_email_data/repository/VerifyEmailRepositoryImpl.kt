package com.example.verify_email_data.repository

import com.example.verify_email_domain.repository.VerifyEmailRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class VerifyEmailRepositoryImpl @Inject constructor(private val firebaseAuth: FirebaseAuth) :
    VerifyEmailRepository {
    override fun sendVerifyEmail() {
        firebaseAuth.currentUser?.sendEmailVerification()
    }

    override fun checkVerifyEmail(): Boolean {
        val user = firebaseAuth.currentUser
        user?.reload()
        return user?.isEmailVerified == true
    }
}