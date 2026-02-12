package com.example.presintation.fragment.splashfragment

import androidx.lifecycle.ViewModel
import com.example.domain.usecase.CheckUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val checkUserUseCase: CheckUserUseCase) :
    ViewModel() {
    fun checkUser() = checkUserUseCase.execute()
}