package com.example.friends_presentation.fragment.friendsfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friends_domain.models.UserModel
import com.example.friends_domain.usecase.GetUserFriendsUseCase
import com.example.friends_domain.usecase.ObserveUserFriendsUseCase
import com.example.friends_domain.utils.Resource
import com.example.friends_presentation.fragment.utils.FriendState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val getUserFriendsUseCase: GetUserFriendsUseCase,
    private val observeUserFriendsUseCase: ObserveUserFriendsUseCase
) : ViewModel() {
    private val _userFriendsState = MutableStateFlow<FriendState>(FriendState.Empty())
    val userFriendsState: StateFlow<FriendState> get() = _userFriendsState

    fun getUserFriends(): List<UserModel> {
        var userFriends = emptyList<UserModel>()
        viewModelScope.launch {
            userFriends = getUserFriendsUseCase.execute()
        }
        return userFriends
    }

    fun observeUserFriends() {
        observeUserFriendsUseCase.execute().onEach { resource ->
            when (resource) {
                is Resource.Loading -> _userFriendsState.value = FriendState.Loading()
                is Resource.ItemChanged -> {
                    when (resource.changeType) {
                        Resource.ChangeType.ADDED -> {
                            if (resource.data != null)
                                _userFriendsState.value = FriendState.Added(data = resource.data!!)
                            else
                                _userFriendsState.value = FriendState.Empty()
                        }

                        Resource.ChangeType.UPDATED -> {
                            if (resource.data != null)
                                _userFriendsState.value =
                                    FriendState.Updated(data = resource.data!!)
                            else
                                _userFriendsState.value = FriendState.Empty()
                        }

                        Resource.ChangeType.DELETED -> {
                            if (resource.data != null)
                                _userFriendsState.value =
                                    FriendState.Deleted(data = resource.data!!)
                            else
                                _userFriendsState.value = FriendState.Empty()
                        }
                    }
                }

                is Resource.Success -> Unit
                is Resource.Error -> _userFriendsState.value = FriendState.Error(resource.message!!)
            }
        }.launchIn(viewModelScope)
    }
}