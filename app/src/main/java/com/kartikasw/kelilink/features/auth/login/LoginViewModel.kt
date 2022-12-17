package com.kartikasw.kelilink.features.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kartikasw.kelilink.core.domain.use_case.auth.AuthUseCase
import com.kartikasw.kelilink.core.domain.use_case.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val userUseCase: UserUseCase
): ViewModel() {

    fun logIn(email: String, password: String, fcmToken: String) =
        authUseCase.logIn(email, password, fcmToken).asLiveData()

    fun getFcmToken() =
        userUseCase.getFcmToken()
}