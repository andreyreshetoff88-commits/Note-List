package com.example.app_data.remote

import com.example.app_data.models.UserModelDto
import com.example.app_data.utils.SyncEvent
import com.example.core.Constants.NODE_USERS
import com.example.core.Constants.NODE_USERS_FRIENDS
import com.example.core.UserSession
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
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

    override fun observeUserProfile(userId: String): Flow<SyncEvent> = callbackFlow {
        userProfileListener?.let {
            firebaseDatabase.child(NODE_USERS).child(userId).removeEventListener(it)
        }

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(UserModelDto::class.java)?.let {
                    trySend(SyncEvent.UserProfile(user = it))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        userProfileListener = listener
        firebaseDatabase.child(NODE_USERS).child(userId)
            .addValueEventListener(listener)

        awaitClose { firebaseDatabase.removeEventListener(listener) }
    }


    override fun observeUsersFriends(userId: String): Flow<SyncEvent> = callbackFlow {
        val listener = object : ChildEventListener {
            override fun onChildAdded(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                val friendId = snapshot.key ?: return
                trySend(SyncEvent.FriendAdded(friendId = friendId, userId = userId))
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val friendId = snapshot.key ?: return
                trySend(SyncEvent.FriendDelete(friendId = friendId, userId = userId))
            }

            override fun onChildChanged(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) = Unit

            override fun onChildMoved(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) = Unit

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }

        }

        firebaseDatabase.child(NODE_USERS_FRIENDS).child(userId)
            .addChildEventListener(listener)

        awaitClose { firebaseDatabase.removeEventListener(listener) }
    }

    override suspend fun getUserById(userId: String): UserModelDto? {
        val snapshot = firebaseDatabase.child(NODE_USERS)
            .child(userId).get().await()

        return snapshot.getValue(UserModelDto::class.java)
    }
}