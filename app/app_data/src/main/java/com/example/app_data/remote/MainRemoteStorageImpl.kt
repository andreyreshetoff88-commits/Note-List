package com.example.app_data.remote

import com.example.app_data.models.UserModelDto
import com.example.core.Constants.NODE_USERS
import com.example.core.Constants.NODE_USER_FRIENDS
import com.example.core.Constants.USER_UID
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
import javax.inject.Inject

class MainRemoteStorageImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: DatabaseReference
) : MainRemoteStorage {
    override fun isAuth(): Boolean {
        val user = firebaseAuth.currentUser
        if (user != null) {
            user.reload()
            if (user.isEmailVerified) {
                USER_UID = firebaseAuth.currentUser!!.uid
                return true
            }
        } else
            return false
        return false
    }

    override fun observeUserProfile(): Flow<UserModelDto> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userModelDto = snapshot.getValue(UserModelDto::class.java) ?: UserModelDto()
                if (userModelDto.id != null)
                    trySend(userModelDto)
            }

            override fun onCancelled(error: DatabaseError) {
                cancel(error.toException().localizedMessage ?: "Ой... Что-то пошло не так")
            }
        }

        firebaseDatabase.child(NODE_USERS).child(USER_UID)
            .addValueEventListener(listener)

        awaitClose { firebaseDatabase.removeEventListener(listener) }
    }


    override fun observeUserFriends(): Flow<String> = callbackFlow {
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

        firebaseDatabase.child(NODE_USER_FRIENDS).child(USER_UID)
            .addChildEventListener(listener)

        awaitClose { firebaseDatabase.removeEventListener(listener) }
    }
}