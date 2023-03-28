package com.kartikasw.kelilink.core.domain.repository

import com.kartikasw.kelilink.core.domain.Resource
import com.kartikasw.kelilink.util.params.RegisterParam
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun register(param: RegisterParam): Flow<Resource<Unit>>
    fun logIn(email: String, password: String, fcmToken: String): Flow<Resource<Unit>>
    fun resetPassword(email: String): Flow<Resource<Unit>>
}