package com.example.kelilink.core.domain.use_case.auth

import com.example.kelilink.core.domain.Resource
import com.example.kelilink.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthInteractor @Inject constructor(private val authRepository: AuthRepository) : AuthUseCase {
    override fun register(email: String, password: String, user: MutableMap<String, Any>): Flow<Resource<Unit>> =
        authRepository.register(email, password, user)

    override fun logIn(email: String, password: String, fcmToken: String): Flow<Resource<Unit>> =
        authRepository.logIn(email, password, fcmToken)

    override fun resetPassword(email: String): Flow<Resource<Unit>> =
        authRepository.resetPassword(email)
}