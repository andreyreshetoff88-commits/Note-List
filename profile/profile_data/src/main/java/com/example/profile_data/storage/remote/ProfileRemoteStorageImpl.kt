package com.example.profile_data.storage.remote

import android.net.Uri
import com.example.core.cloudinary.CloudinaryUploader
import com.example.core.Constants.NODE_USERS
import com.example.core.UserSession
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileRemoteStorageImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: DatabaseReference,
    private val userSession: UserSession,
    private val cloudinaryUploader: CloudinaryUploader
) : ProfileRemoteStorage {
    override suspend fun editFirstName(firstName: String): Result<Unit> =
        suspendCancellableCoroutine { cont ->
            val userRef = firebaseDatabase.child(NODE_USERS)
                .child(userSession.userId.value!!)
                .child("firstName")

            userRef.setValue(firstName)
                .addOnSuccessListener {
                    cont.resumeWith(Result.success(Result.success(Unit)))
                }
                .addOnFailureListener { e ->
                    cont.resumeWith(Result.success(Result.failure(e)))
                }

            cont.invokeOnCancellation {
                userRef.onDisconnect()
            }
        }

    override suspend fun editLastName(lastName: String): Result<Unit> =
        suspendCancellableCoroutine { cont ->
            val userRef = firebaseDatabase.child(NODE_USERS)
                .child(userSession.userId.value!!)
                .child("lastName")

            userRef.setValue(lastName)
                .addOnSuccessListener {
                    cont.resumeWith(Result.success(Result.success(Unit)))
                }
                .addOnFailureListener { e ->
                    cont.resumeWith(Result.success(Result.failure(e)))
                }

            cont.invokeOnCancellation {
                userRef.onDisconnect()
            }
        }

    override suspend fun changeUserPhoto(uri: Uri): Result<Unit> {
        return try {
            val imageUrl = cloudinaryUploader.uploadImage(
                uri = uri,
                preset = "avatar_preset",
                folderName = "avatar"
            )
            firebaseDatabase.child(NODE_USERS)
                .child(userSession.userId.value!!)
                .child("userPhoto")
                .setValue(imageUrl).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun changePassword(oldPassword: String, newPassword: String): Result<Unit> =
        suspendCancellableCoroutine { cont ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                cont.resumeWith(Result.success(Result.failure(Exception("Пользователь не авторизован"))))
                return@suspendCancellableCoroutine
            }

            val email = user.email
            if (email == null) {
                cont.resumeWith(Result.success(Result.failure(Exception("Email не найден"))))
                return@suspendCancellableCoroutine
            }

            val credential = EmailAuthProvider.getCredential(email, oldPassword)

            user.reauthenticate(credential).addOnSuccessListener {
                user.updatePassword(newPassword).addOnSuccessListener {
                    firebaseAuth.signOut()
                    cont.resumeWith(Result.success(Result.success(Unit)))
                }.addOnFailureListener { e ->
                    cont.resumeWith(Result.success(Result.failure(e)))
                }
            }.addOnFailureListener { e ->
                val message = when (e) {
                    is FirebaseAuthException -> when (e.errorCode) {
                        "ERROR_WRONG_PASSWORD" ->
                            "Старый пароль введён неверно"

                        "ERROR_USER_MISMATCH" ->
                            "Неверные учетные данные пользователя"

                        "ERROR_USER_NOT_FOUND" ->
                            "Пользователь не найден"

                        "ERROR_INVALID_CREDENTIAL" ->
                            "Неверный пароль"

                        else -> e.localizedMessage ?: "Ошибка авторизации"
                    }

                    else -> e.localizedMessage ?: "Неизвестная ошибка"
                }
                cont.resumeWith(
                    Result.success(Result.failure(Exception(message)))
                )
            }
        }

    override suspend fun signOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}