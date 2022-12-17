package com.kartikasw.kelilink.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kartikasw.kelilink.core.domain.use_case.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userUseCase: UserUseCase
): ViewModel() {

    fun logout(): Unit =
        userUseCase.logout()

    fun getMyProfile() =
        userUseCase.getMyProfileFromInternet().asLiveData()
}