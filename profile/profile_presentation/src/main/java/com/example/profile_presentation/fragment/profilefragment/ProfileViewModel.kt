package com.example.profile_presentation.fragment.profilefragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.State
import com.example.profile_domain.models.UserModel
import com.example.profile_domain.usecase.EditFirstNameUseCase
import com.example.profile_domain.usecase.GetUserInfoUseCase
import com.example.profile_domain.usecase.SignOutUseCase
import com.example.profile_domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val editFirstNameUseCase: EditFirstNameUseCase
) : ViewModel() {
    private var _signOutState = MutableStateFlow<State<Boolean>>(State.Empty())
    val signOutState: StateFlow<State<Boolean>> get() = _signOutState
    private var _userInfoState = MutableStateFlow<State<UserModel>>(State.Empty())
    val userInfoState: StateFlow<State<UserModel>> get() = _userInfoState
    private var _editUserInfoState = MutableStateFlow<State<Boolean>>(State.Empty())
    val editUserInfoState: StateFlow<State<Boolean>> get() = _editUserInfoState

    suspend fun getUserInfo() {
        getUserInfoUseCase.execute().onEach {
            when (it) {
                is Resource.Loading -> _userInfoState.value = State.Loading()
                is Resource.Success -> {
                    _userInfoState.value = State.Success(data = it.data)
                }

                is Resource.Error -> _userInfoState.value = State.Error(message = it.message)
            }
        }.launchIn(viewModelScope)
    }

    suspend fun editFirstName(firstName: String) {
        editFirstNameUseCase.execute(firstName = firstName).onEach {
            when (it) {
                is Resource.Loading -> _editUserInfoState.value = State.Loading()
                is Resource.Success -> {
                    _editUserInfoState.value = State.Success(data = it.data?.success)
                }

                is Resource.Error -> _editUserInfoState.value = State.Error(message = it.message)
            }
        }.launchIn(viewModelScope)
    }

    suspend fun signOut() {
        signOutUseCase.execute().onEach {
            when (it) {
                is Resource.Loading -> _signOutState.value = State.Loading()
                is Resource.Success -> {
                    if (it.data == true)
                        _signOutState.value = State.Success(data = it.data)
                }

                is Resource.Error -> _signOutState.value = State.Error(message = it.message)
            }
        }.launchIn(viewModelScope)
    }
}