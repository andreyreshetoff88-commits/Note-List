package com.example.presintation.fragment.registerfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.State
import com.example.domain.models.UserModel
import com.example.domain.usecase.RegisterUserUseCase
import com.example.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {
    private var _viewState = MutableStateFlow<State<Unit>>(State.Empty())
    val viewState: StateFlow<State<Unit>> get() = _viewState

    fun registerUser(userModel: UserModel, password: String) {
        registerUserUseCase.execute(userModel = userModel, password = password)
            .onEach { registerState ->
                when (registerState) {
                    is Resource.Loading -> _viewState.value = State.Loading()
                    is Resource.Success -> {
                        _viewState.value = State.Success(Unit)
                    }

                    is Resource.Error -> _viewState.value =
                        State.Error(message = registerState.message)
                }
            }.launchIn(viewModelScope)
    }
}