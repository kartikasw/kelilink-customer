package com.kartikasw.kelilink.features.profile.edit_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kartikasw.kelilink.core.domain.use_case.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditPasswordViewModel  @Inject constructor(
    private val userUseCase: UserUseCase
): ViewModel() {

    fun updatePassword(oldPassword: String, newPassword: String) =
        userUseCase.updatePassword(oldPassword, newPassword).asLiveData()

}