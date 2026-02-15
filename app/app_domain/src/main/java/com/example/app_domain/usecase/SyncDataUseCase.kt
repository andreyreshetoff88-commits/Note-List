package com.example.app_domain.usecase

import com.example.app_domain.repository.MainRepository
import javax.inject.Inject

class SyncDataUseCase @Inject constructor(private val mainRepository: MainRepository){
    suspend fun execute() = mainRepository.syncData()
}