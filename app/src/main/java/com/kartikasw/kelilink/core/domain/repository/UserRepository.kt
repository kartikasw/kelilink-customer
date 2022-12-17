package com.kartikasw.kelilink.core.domain.repository

import com.kartikasw.kelilink.core.data.source.remote.response.UserResponse
import com.kartikasw.kelilink.core.domain.Resource
import com.kartikasw.kelilink.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getMyProfileFromLocal(): Flow<Resource<User>>
    fun getMyProfileFromInternet(): Flow<Resource<User>>
    fun updateMyProfile(data: MutableMap<String, Any>): Flow<Resource<Unit>>
    fun updateUserLocation(uid: String, latitude: Double, longitude: Double)
    fun logout()

    fun setFcmToken(token: String)
    fun getFcmToken(): String

    fun updatePassword(oldPassword: String, newPassword: String): Flow<Resource<UserResponse>>

}