package com.kartikasw.kelilink.features.profile.edit_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kartikasw.kelilink.core.domain.use_case.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userUseCase: UserUseCase
): ViewModel() {

    fun updateMyProfile(data: MutableMap<String, Any>) =
        userUseCase.updateMyProfile(data).asLiveData()

}