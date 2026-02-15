package com.example.profile_data.storage

import com.example.core.Constants.NODE_USERS
import com.example.core.Constants.USER_UID
import com.example.profile_data.models.DataEditModelDto
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
    override suspend fun editFirstName(firstName: String): DataEditModelDto {
        try {
            firebaseDatabase.child(NODE_USERS)
                .child(USER_UID)
                .child("firstName").setValue(firstName).await()
            return DataEditModelDto(success = true)
        }catch (e: Exception){
            return DataEditModelDto(error = e.localizedMessage)
        }
    }

    override suspend fun editLastName(lastName: String): DataEditModelDto {
        try {
            firebaseDatabase.child(NODE_USERS)
                .child(USER_UID)
                .child("lastName").setValue(lastName).await()
            return DataEditModelDto(success = true)
        }catch (e: Exception){
            return DataEditModelDto(error = e.localizedMessage)
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