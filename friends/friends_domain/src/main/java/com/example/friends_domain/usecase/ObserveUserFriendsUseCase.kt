package com.example.friends_domain.usecase

import com.example.friends_domain.repository.FriendsRepository
import javax.inject.Inject

class ObserveUserFriendsUseCase @Inject constructor(private val friendsRepository: FriendsRepository) {
    fun execute() = friendsRepository.observeUserFriends()
}