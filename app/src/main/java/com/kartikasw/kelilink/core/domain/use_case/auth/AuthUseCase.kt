package com.kartikasw.kelilink.core.domain.use_case.auth

import com.kartikasw.kelilink.core.domain.Resource
import kotlinx.coroutines.flow.Flow

interface AuthUseCase {
    fun register(email: String, password: String, user: MutableMap<String, Any>): Flow<Resource<Unit>>
    fun logIn(email: String, password: String, fcmToken: String): Flow<Resource<Unit>>
    fun resetPassword(email: String): Flow<Resource<Unit>>
}