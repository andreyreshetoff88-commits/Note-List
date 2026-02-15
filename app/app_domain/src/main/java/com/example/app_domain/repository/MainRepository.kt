package com.example.app_domain.repository

interface MainRepository {
    suspend fun syncData()
}