package com.example.kelilink.core.domain.use_case.user

import com.example.kelilink.core.data.source.remote.response.UserResponse
import com.example.kelilink.core.domain.Resource
import com.example.kelilink.core.domain.model.User
import com.example.kelilink.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserInteractor @Inject constructor(
    private val userRepository: UserRepository
) : UserUseCase {
    override fun getMyProfileFromLocal(): Flow<Resource<User>> =
        userRepository.getMyProfileFromLocal()

    override fun getMyProfileFromInternet(): Flow<Resource<User>> =
        userRepository.getMyProfileFromInternet()

    override fun updateMyProfile(data: MutableMap<String, Any>): Flow<Resource<Unit>> =
        userRepository.updateMyProfile(data)

    override fun updateUserLocation(uid: String, latitude: Double, longitude: Double) =
        userRepository.updateUserLocation(uid, latitude, longitude)

    override fun logout() =
        userRepository.logout()

    override fun setFcmToken(token: String) {
        userRepository.setFcmToken(token)
    }

    override fun getFcmToken(): String =
        userRepository.getFcmToken()

    override fun updatePassword(oldPassword: String, newPassword: String): Flow<Resource<UserResponse>> =
        userRepository.updatePassword(oldPassword, newPassword)
}