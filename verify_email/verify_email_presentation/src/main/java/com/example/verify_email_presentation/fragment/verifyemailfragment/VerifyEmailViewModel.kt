package com.example.verify_email_presentation.fragment.verifyemailfragment

import androidx.lifecycle.ViewModel
import com.example.verify_email_domain.usecase.CheckVerifyEmailUseCase
import com.example.verify_email_domain.usecase.SendVerifyEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VerifyEmailViewModel @Inject constructor(
    private val sendVerifyEmailUseCase: SendVerifyEmailUseCase,
    private val checkVerifyEmailUseCase: CheckVerifyEmailUseCase
): ViewModel() {
    fun sendVerifyEmail() = sendVerifyEmailUseCase.execute()

    fun checkVerifyEmail() = checkVerifyEmailUseCase.execute()
}