package com.example.kelilink.core.data.source.remote.service.firebase

import com.example.kelilink.core.data.helper.Constants.DatabaseCollection.USER_COLLECTION
import com.example.kelilink.core.data.helper.Response
import com.example.kelilink.core.data.source.remote.response.UserResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserService @Inject constructor(): FirebaseService() {

    fun getMyProfile(): Flow<Response<UserResponse>> =
        flow {
            val id = getUser()!!.uid
            emitAll(getDocumentById(USER_COLLECTION, id))
        }

    fun updateMyProfile(data: MutableMap<String, Any>): Flow<Response<UserResponse>> =
        updateDocument(USER_COLLECTION, getUser()!!.uid, data)

    fun logout() : Unit =
        signOut()

    fun updatePassword(oldPassword: String, newPassword: String): Flow<Response<UserResponse>> =
        reAuthenticate(oldPassword, newPassword)

}