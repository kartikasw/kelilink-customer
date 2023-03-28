package com.kartikasw.kelilink.core.domain.use_case.auth

import com.kartikasw.kelilink.core.domain.Resource
import com.kartikasw.kelilink.core.domain.repository.AuthRepository
import com.kartikasw.kelilink.util.params.RegisterParam
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthInteractor @Inject constructor(private val authRepository: AuthRepository) : AuthUseCase {
    override fun register(param: RegisterParam): Flow<Resource<Unit>> =
        authRepository.register(param)

    override fun logIn(email: String, password: String, fcmToken: String): Flow<Resource<Unit>> =
        authRepository.logIn(email, password, fcmToken)

    override fun resetPassword(email: String): Flow<Resource<Unit>> =
        authRepository.resetPassword(email)
}