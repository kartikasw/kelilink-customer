package com.kartikasw.kelilink.core.domain.use_case.auth

import com.kartikasw.kelilink.core.domain.Resource
import com.kartikasw.kelilink.util.params.RegisterParam
import kotlinx.coroutines.flow.Flow

interface AuthUseCase {
    fun register(param: RegisterParam): Flow<Resource<Unit>>
    fun logIn(email: String, password: String, fcmToken: String): Flow<Resource<Unit>>
    fun resetPassword(email: String): Flow<Resource<Unit>>
}