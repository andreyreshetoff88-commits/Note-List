package com.example.friends_data.models

import com.example.core.room.entitys.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class FriendDiff(
    val added: List<UserEntity>,
    val removed: List<UserEntity>,
    val updated: List<UserEntity>
)

fun Flow<List<UserEntity>>.diff(): Flow<FriendDiff> = flow {
    var oldList: List<UserEntity> = emptyList()

    collect { newList ->
        val oldMap = oldList.associateBy { it.id }
        val newMap = newList.associateBy { it.id }

        val added = newList.filter { it.id !in oldMap }
        val removed = oldList.filter { it.id !in newMap }
        val updated = newList.filter { oldMap[it.id] != it }

        emit(FriendDiff(added = added, removed = removed, updated = updated))
        oldList = newList
    }
}
