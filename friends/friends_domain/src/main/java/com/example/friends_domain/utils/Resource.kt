package com.example.friends_domain.utils

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Loading<T> : Resource<T>()
    class Success<T>(data: T?) : Resource<T>(data = data)
    class Error<T>(message: String?) : Resource<T>(message = message)

    class ItemChanged<T>(data: T, val changeType: ChangeType) : Resource<T>(data = data)
    enum class ChangeType { ADDED, UPDATED, DELETED }
}