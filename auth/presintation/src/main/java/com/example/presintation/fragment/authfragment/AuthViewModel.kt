package com.example.presintation.fragment.authfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.State
import com.example.domain.usecase.CheckVerifyUseCase
import com.example.domain.usecase.LoginUserUseCase
import com.example.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val checkVerifyUseCase: CheckVerifyUseCase
) : ViewModel() {
    private var _viewState = MutableStateFlow<State<Boolean>>(State.Empty())
    val viewState: StateFlow<State<Boolean>> get() = _viewState

    suspend fun loginUser(email: String, password: String) {
        loginUserUseCase.execute(email = email, password = password).onEach {
            when (it) {
                is Resource.Loading -> _viewState.value = State.Loading()
                is Resource.Success -> {
                    if (it.data == null)
                        _viewState.value = State.Empty()
                    else
                        _viewState.value = State.Success(data = it.data)
                }

                is Resource.Error -> _viewState.value = State.Error(message = it.message)
            }
        }.launchIn(viewModelScope)
    }

    fun checkVerifyEmail() = checkVerifyUseCase.execute()
}