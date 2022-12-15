package com.example.kelilink.features.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kelilink.core.domain.use_case.auth.AuthUseCase
import com.example.kelilink.core.domain.use_case.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val userUseCase: UserUseCase
): ViewModel() {

    fun register(email: String, password: String, user: MutableMap<String, Any>) =
        authUseCase.register(email, password, user).asLiveData()

    fun getFcmToken() =
        userUseCase.getFcmToken()
}