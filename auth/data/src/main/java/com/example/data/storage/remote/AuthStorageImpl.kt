package com.example.data.storage.remote

import com.example.core.Constants.NODE_USERS
import com.example.core.UserSession
import com.example.data.models.UserModelDto
import com.example.domain.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthStorageImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: DatabaseReference,
    private val userSession: UserSession
) : AuthStorage {
    private val message = "Ой, что-то пошло не так"

    override fun loginUser(email: String, password: String) = flow {
        emit(Resource.Loading())
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            userSession.setUser(firebaseAuth.currentUser?.uid)
            getUser()
                .onSuccess { user -> emit(Resource.Success(data = user)) }
                .onFailure { emit(Resource.Error(message = message)) }
        } catch (e: Exception) {
            val errorMessage = if (e is FirebaseAuthException) {
                getErrorText(e)
            } else {
                e.message ?: message
            }
            emit(Resource.Error(message = errorMessage))
        }
    }

    override fun registerUser(
        userModelDto: UserModelDto,
        password: String
    ) = flow {
        emit(Resource.Loading())
        try {
            firebaseAuth.createUserWithEmailAndPassword(userModelDto.email, password).await()
            createNewUser(userModelDto).onSuccess {
                userSession.setUser(firebaseAuth.currentUser?.uid)
                firebaseAuth.currentUser?.sendEmailVerification()?.await()
                getUser()
                    .onSuccess { user -> emit(Resource.Success(data = user)) }
                    .onFailure { e -> emit(Resource.Error(message = e.message)) }
            }.onFailure { emit(Resource.Error(message = it.message)) }
        } catch (e: Exception) {
            val errorMessage = if (e is FirebaseAuthException) {
                getErrorText(e)
            } else {
                e.message ?: message
            }
            emit(Resource.Error(message = errorMessage))
        }
    }

    private suspend fun createNewUser(userModelDto: UserModelDto): Result<Unit> =
        suspendCancellableCoroutine { cont ->
            userModelDto.id = firebaseAuth.currentUser?.uid
            val ref = firebaseDatabase.child(NODE_USERS).child(userModelDto.id!!)

            ref.setValue(userModelDto).addOnSuccessListener {
                cont.resumeWith(Result.success(Result.success(Unit)))
            }.addOnFailureListener { e ->
                cont.resumeWith(Result.success(Result.failure(e)))
            }

            cont.invokeOnCancellation {
                ref.onDisconnect()
            }
        }

    override fun checkVerifyEmail(): Boolean {
        val user = firebaseAuth.currentUser
        user?.reload()
        return user?.isEmailVerified == true
    }

    override suspend fun getUser(): Result<UserModelDto> {
        return try {
            val uid = userSession.userId.value
                ?: return Result.failure(Exception("Пользователь не авторизован"))

            val snapshot = firebaseDatabase.child(NODE_USERS)
                .child(uid).get().await()
            val user = snapshot.getValue(UserModelDto::class.java) ?: return Result.failure(
                Exception("Данные пользователя не найдены")
            )

            Result.success(user)
        } catch (e: Exception) {
            return Result.failure(e)
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