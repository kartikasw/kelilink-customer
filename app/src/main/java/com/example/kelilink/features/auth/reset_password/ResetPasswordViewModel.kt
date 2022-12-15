package com.example.kelilink.features.auth.reset_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kelilink.core.domain.use_case.auth.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
): ViewModel() {
    fun resetPassword(email: String) =
        authUseCase.resetPassword(email).asLiveData()
}