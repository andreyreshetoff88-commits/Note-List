package com.example.friends_data.repository

import com.example.friends_data.storage.local.FriendsLocalStorage
import com.example.friends_data.utils.mapToUserModel
import com.example.friends_domain.models.UserModel
import com.example.friends_domain.repository.FriendsRepository
import com.example.friends_domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FriendsRepositoryImpl @Inject constructor(
    private val friendsLocalStorage: FriendsLocalStorage
) : FriendsRepository {
    override suspend fun getUserFriends() =
        friendsLocalStorage.getUserFriends().map { it.mapToUserModel() }

    override fun observeUserFriends(): Flow<Resource<UserModel>> {
        return friendsLocalStorage.observeUserFriends().flatMapConcat { diff ->
            flow {
                diff.added.forEach {
                    emit(
                        Resource.ItemChanged(
                            data = it.mapToUserModel(),
                            changeType = Resource.ChangeType.ADDED
                        )
                    )
                }
                diff.updated.forEach {
                    emit(
                        Resource.ItemChanged(
                            data = it.mapToUserModel(),
                            changeType = Resource.ChangeType.UPDATED
                        )
                    )
                }
                diff.removed.forEach {
                    emit(
                        Resource.ItemChanged(
                            data = it.mapToUserModel(),
                            changeType = Resource.ChangeType.DELETED
                        )
                    )
                }
            }
        }
    }
}