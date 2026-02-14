package com.example.profile_data.storage

import com.example.core.Constants.NODE_USERS
import com.example.profile_data.models.UserModelDto
import com.example.profile_domain.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileStorageImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: DatabaseReference
) : ProfileStorage {
    override suspend fun getUserInfo(): UserModelDto {
        try {
            val snapshot = firebaseDatabase.child(NODE_USERS)
                .child(firebaseAuth.currentUser!!.uid).get().await()
            val id = snapshot.child("id").getValue(String::class.java)
            val firstName = snapshot.child("firstName").getValue(String::class.java)
            val lastName = snapshot.child("lastName").getValue(String::class.java)
            val email = snapshot.child("email").getValue(String::class.java)
            val userPhoto = snapshot.child("userPhoto").getValue(String::class.java)

            return UserModelDto(
                id = id,
                firstName = firstName,
                lastName = lastName,
                email = email,
                userPhoto = userPhoto
            )
        } catch (e: Exception) {
            return UserModelDto(error = e.localizedMessage)
        }
    }

    override suspend fun editFirstName(firstName: String): UserModelDto {
        try {
            firebaseDatabase.child(NODE_USERS)
                .child(firebaseAuth.currentUser!!.uid)
                .child("firstName").setValue(firstName).await()
            return getUserInfo()
        }catch (e: Exception){
            return UserModelDto(error = e.localizedMessage)
        }
    }

    override suspend fun editLastName(lastName: String): UserModelDto {
        try {
            firebaseDatabase.child(NODE_USERS)
                .child(firebaseAuth.currentUser!!.uid)
                .child("lastName").setValue(lastName).await()
            return getUserInfo()
        }catch (e: Exception){
            return UserModelDto(error = e.localizedMessage)
        }
    }

    override suspend fun signOut() = flow {
        emit(Resource.Loading())
        try {
            firebaseAuth.signOut()

            emit(Resource.Success(data = true))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage))
        }
    }
}