package com.example.data.storage

import com.example.core.Constants.NODE_USERS
import com.example.data.models.UserModelDto
import com.example.domain.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthStorageImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: DatabaseReference
) : AuthStorage {
    private val message = "Ой, что-то пошло не так"

    override suspend fun loginUser(email: String, password: String) = flow {
        emit(Resource.Loading())
        try {
            val resultAuth = firebaseAuth.signInWithEmailAndPassword(email, password).await()

            if (resultAuth != null)
                emit(Resource.Success(data = true))
            else
                emit(Resource.Error(message = message))
        } catch (e: Exception) {
            emit(Resource.Error(message = getErrorText(e as FirebaseAuthException)))
        }
    }

    override suspend fun registerUser(userModelDto: UserModelDto, password: String) = flow {
        emit(Resource.Loading())
        try {
            firebaseAuth.createUserWithEmailAndPassword(userModelDto.email, password).await()
            createNewUser(userModelDto).collect {
                when (it) {
                    is Resource.Loading -> emit(Resource.Loading())
                    is Resource.Success -> emit(Resource.Success(it.data))
                    is Resource.Error -> emit(Resource.Error(message = it.message))
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = getErrorText(e as? FirebaseAuthException)))
        }
    }

    private fun createNewUser(userModelDto: UserModelDto) = flow {
        emit(Resource.Loading())
        userModelDto.id = firebaseAuth.currentUser?.uid
        try {
            firebaseDatabase.child(NODE_USERS).child(userModelDto.id!!)
                .setValue(userModelDto).await()

            emit(Resource.Success(data = true))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage))
        }
    }

    private fun getErrorText(error: FirebaseAuthException?) = when (error?.errorCode) {
        "ERROR_INVALID_EMAIL" -> "Некорректный email"
        "ERROR_WRONG_PASSWORD" -> "Неверный пароль"
        "ERROR_USER_NOT_FOUND", "ERROR_INVALID_CREDENTIAL" -> "Пользователь не найден"
        "ERROR_EMAIL_ALREADY_IN_USE" -> "Email уже используется"
        "ERROR_NETWORK_REQUEST_FAILED" -> "Проблема с интернет-соединением."
        "ERROR_USER_DISABLED" -> "Аккаунт пользователя отключён."
        "ERROR_WEAK_PASSWORD" -> "Пароль слишком простой"
        "ERROR_TOO_MANY_REQUESTS" -> "Слишком много попыток входа"
        else -> error?.localizedMessage
    }
}