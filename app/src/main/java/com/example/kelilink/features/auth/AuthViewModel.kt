package com.example.kelilink.features.auth

import androidx.lifecycle.ViewModel
import com.example.kelilink.core.domain.use_case.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userUseCase: UserUseCase
): ViewModel() {

    fun setFcmToken(token: String) =
        userUseCase.setFcmToken(token)

}