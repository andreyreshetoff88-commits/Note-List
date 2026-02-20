package com.example.profile_data.storage

import com.example.core.Constants.NODE_USERS
import com.example.core.UserSession
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class ProfileStorageImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: DatabaseReference,
    private val userSession: UserSession
) : ProfileStorage {
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
                cont.resumeWith(
                    Result.success(Result.failure(e))
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