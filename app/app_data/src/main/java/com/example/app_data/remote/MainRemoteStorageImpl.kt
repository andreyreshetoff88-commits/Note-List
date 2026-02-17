package com.example.app_data.remote

import android.util.Log
import com.example.app_data.models.UserModelDto
import com.example.core.Constants.NODE_USERS
import com.example.core.Constants.NODE_USER_FRIENDS
import com.example.core.UserSession
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MainRemoteStorageImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: DatabaseReference,
    private val userSession: UserSession
) : MainRemoteStorage {
    private var userProfileListener: ValueEventListener? = null

    override suspend fun isAuth(): Boolean {
        val user = firebaseAuth.currentUser ?: return false
        user.reload().await()
        return if (user.isEmailVerified) {
            userSession.setUser(firebaseAuth.currentUser?.uid)
            true
        } else {
            false
        }
    }

    override fun observeUserProfile(userId: String): Flow<UserModelDto> = callbackFlow {
        userProfileListener?.let {
            firebaseDatabase.child(NODE_USERS).child(userId).removeEventListener(it)
        }

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userModelDto = snapshot.getValue(UserModelDto::class.java) ?: UserModelDto()
                Log.d("ololo", "onDataChange: $userModelDto")
                if (userModelDto.id != null)
                    trySend(userModelDto)
            }

            override fun onCancelled(error: DatabaseError) {
                cancel(error.toException().localizedMessage ?: "Ой... Что-то пошло не так")
            }
        }

        userProfileListener = listener
        firebaseDatabase.child(NODE_USERS).child(userId)
            .addValueEventListener(listener)

        awaitClose { firebaseDatabase.removeEventListener(listener) }
    }


    override fun observeUserFriends(userId: String): Flow<String> = callbackFlow {
        val listener = object : ChildEventListener {
            override fun onChildAdded(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {

            }

            override fun onChildChanged(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {

            }

            override fun onCancelled(error: DatabaseError) {
                cancel(error.toException().localizedMessage ?: "Ой... Что-то пошло не так")
            }

        }

        firebaseDatabase.child(NODE_USER_FRIENDS).child(userId)
            .addChildEventListener(listener)

        awaitClose { firebaseDatabase.removeEventListener(listener) }
    }
}