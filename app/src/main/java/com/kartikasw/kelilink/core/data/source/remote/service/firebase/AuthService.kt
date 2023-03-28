package com.kartikasw.kelilink.core.data.source.remote.service.firebase

import com.kartikasw.kelilink.core.data.helper.Constants.DatabaseCollection.USER_COLLECTION
import com.kartikasw.kelilink.core.data.helper.Constants.DatabaseColumn.FCM_TOKEN_COLUMN
import com.kartikasw.kelilink.core.data.helper.Constants.DatabaseColumn.UID_COLUMN
import com.kartikasw.kelilink.core.data.helper.Response
import com.kartikasw.kelilink.core.data.source.remote.response.UserResponse
import com.kartikasw.kelilink.util.params.RegisterParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthService @Inject constructor(): FirebaseService() {

    fun register(param: RegisterParam): Flow<Response<UserResponse>> =
        flow {
            createUserWithEmailAndPassword(param.email, param.password).collect {
                when(it) {
                    is Response.Success -> {
                        param.user[UID_COLUMN] = it.data
                        emitAll(
                            setDocument(
                                USER_COLLECTION,
                                it.data,
                                param.user
                            )
                        )
                    }
                    is Response.Error -> {
                        emit(Response.Error(it.errorMessage))
                    }
                    is Response.Empty -> {
                        emit(Response.Empty)
                    }
                }
            }
        }

    fun logIn(email: String, password: String, fcmToken: String): Flow<Response<UserResponse>> =
        flow {
            signInWithEmailAndPassword(email, password).collect {
                when(it) {
                    is Response.Success -> {
                        emitAll(
                            updateFieldInDocument(
                                USER_COLLECTION,
                                it.data,
                                FCM_TOKEN_COLUMN,
                                fcmToken
                            )
                        )
                    }
                    is Response.Error -> {
                        emit(Response.Error(it.errorMessage))
                    }
                    is Response.Empty -> {
                        emit(Response.Empty)
                    }
                }
            }
        }

    fun resetPassword(email: String): Flow<Response<Unit>> =
        sendPasswordResetEmail(email)

}