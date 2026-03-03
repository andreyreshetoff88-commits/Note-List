package com.example.friends_domain.usecase

import com.example.friends_domain.repository.FriendsRepository
import javax.inject.Inject

class GetUserFriendsUseCase @Inject constructor(
    private val friendsRepository: FriendsRepository
) {
    suspend fun execute() = friendsRepository.getUserFriends()
}