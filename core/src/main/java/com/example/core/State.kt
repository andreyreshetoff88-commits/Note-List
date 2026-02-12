package com.example.core

sealed class State<T>(
    val data: T? = null,
    val message: String? = null
) {

    class Loading<T> : State<T>()
    class Success<T>(data: T?) : State<T>(data = data)
    class Empty<T> : State<T>()
    class Error<T>(message: String?) : State<T>(message = message)

}