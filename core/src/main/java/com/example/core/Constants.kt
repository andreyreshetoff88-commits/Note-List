package com.example.core

import android.Manifest
import androidx.appcompat.app.AppCompatActivity

object Constants {
    val PASSWORD_REGEX =
        Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[_-])[A-Za-z\\d_-]{8,}$")
    lateinit var APP_ACTIVITY: AppCompatActivity

    const val USERS = "users"
    const val NODE_USERS = "users"
    const val NODE_PHONES = "phones"

    const val CHILD_USERS_CONTACTS = "users_contacts"

    const val READ_CONTACTS = Manifest.permission.READ_CONTACTS
}