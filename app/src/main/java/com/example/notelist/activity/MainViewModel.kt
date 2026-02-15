package com.example.notelist.activity

import androidx.lifecycle.ViewModel
import com.example.app_domain.usecase.SyncDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val syncDataUseCase: SyncDataUseCase): ViewModel() {
    suspend fun syncData() = syncDataUseCase.execute()
}