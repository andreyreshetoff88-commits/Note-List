package com.example.presintation.fragment.splashfragment

import androidx.lifecycle.ViewModel
import com.example.domain.usecase.CheckUserUseCase
import com.example.domain.usecase.CheckVerifyEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkUserUseCase: CheckUserUseCase,
    private val checkVerifyEmailUseCase: CheckVerifyEmailUseCase
) :
    ViewModel() {
    fun checkUser() = checkUserUseCase.execute()

    fun checkVerifyEmail() = checkVerifyEmailUseCase.execute()
}