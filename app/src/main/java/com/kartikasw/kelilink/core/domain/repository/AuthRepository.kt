package com.kartikasw.kelilink.core.domain.repository

import com.kartikasw.kelilink.core.domain.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun register(email: String, password: String, user: MutableMap<String, Any>): Flow<Resource<Unit>>
    fun logIn(email: String, password: String, fcmToken: String): Flow<Resource<Unit>>
    fun resetPassword(email: String): Flow<Resource<Unit>>
}