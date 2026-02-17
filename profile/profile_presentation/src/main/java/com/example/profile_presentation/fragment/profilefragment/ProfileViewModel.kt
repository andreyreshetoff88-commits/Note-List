package com.example.profile_presentation.fragment.profilefragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.State
import com.example.profile_domain.models.UserModel
import com.example.profile_domain.usecase.EditFirstNameUseCase
import com.example.profile_domain.usecase.EditLastNameUseCase
import com.example.profile_domain.usecase.GetUserInfoUseCase
import com.example.profile_domain.usecase.SignOutUseCase
import com.example.profile_domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    getUserInfoUseCase: GetUserInfoUseCase,
    private val editFirstNameUseCase: EditFirstNameUseCase,
    private val editLastNameUseCase: EditLastNameUseCase
) : ViewModel() {
    private var _signOutState = MutableStateFlow<State<Unit>>(State.Empty())
    val signOutState: StateFlow<State<Unit>> get() = _signOutState
    private var _editUserInfoState = MutableStateFlow<State<Unit>>(State.Empty())
    val editUserInfoState: StateFlow<State<Unit>> get() = _editUserInfoState
    val userInfoState: StateFlow<State<UserModel>> = getUserInfoUseCase.execute().map { resource ->
        when (resource) {
            is Resource.Loading -> State.Loading()
            is Resource.Success -> State.Success(data = resource.data)
            is Resource.Error -> State.Error(message = resource.message)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = State.Empty()
    )


    suspend fun editFirstName(firstName: String) {
        editFirstNameUseCase.execute(firstName = firstName).onEach {
            when (it) {
                is Resource.Loading -> _editUserInfoState.value = State.Loading()
                is Resource.Success -> _editUserInfoState.value = State.Success(data = it.data)
                is Resource.Error -> _editUserInfoState.value = State.Error(message = it.message)
            }
        }.launchIn(viewModelScope)
    }

    suspend fun editLastName(lastName: String) {
        editLastNameUseCase.execute(lastName = lastName).onEach {
            when (it) {
                is Resource.Loading -> _editUserInfoState.value = State.Loading()
                is Resource.Success -> _editUserInfoState.value = State.Success(data = it.data)
                is Resource.Error -> _editUserInfoState.value = State.Error(message = it.message)
            }
        }.launchIn(viewModelScope)
    }


    suspend fun signOut() {
        signOutUseCase.execute().onEach {
            when (it) {
                is Resource.Loading -> _signOutState.value = State.Loading()
                is Resource.Success -> _signOutState.value = State.Success(data = it.data)
                is Resource.Error -> _signOutState.value = State.Error(message = it.message)
            }
        }.launchIn(viewModelScope)
    }
}