package com.example.data.repository

import com.example.domain.repository.SplashRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class SplashRepositoryIml @Inject constructor(private val firebaseAuth: FirebaseAuth) :
    SplashRepository {
    override fun checkUser(): Boolean {
//        firebaseAuth.signOut()
        return firebaseAuth.currentUser != null
    }
}