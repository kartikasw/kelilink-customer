package com.kartikasw.kelilink.features.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kartikasw.kelilink.core.domain.use_case.auth.AuthUseCase
import com.kartikasw.kelilink.core.domain.use_case.user.UserUseCase
import com.kartikasw.kelilink.util.params.RegisterParam
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val userUseCase: UserUseCase
): ViewModel() {

    fun register(param: RegisterParam) =
        authUseCase.register(param).asLiveData()

    fun getFcmToken() =
        userUseCase.getFcmToken()
}