package com.kartikasw.kelilink.core.data.repository

import com.kartikasw.kelilink.core.data.helper.Response
import com.kartikasw.kelilink.core.data.mapper.toModel
import com.kartikasw.kelilink.core.data.source.local.LocalDataSource
import com.kartikasw.kelilink.core.data.source.remote.RemoteDataSource
import com.kartikasw.kelilink.core.data.source.remote.response.UserResponse
import com.kartikasw.kelilink.core.domain.Resource
import com.kartikasw.kelilink.core.domain.model.User
import com.kartikasw.kelilink.core.domain.repository.UserRepository
import com.kartikasw.kelilink.core.util.AppExecutors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val remote: RemoteDataSource,
    private val local: LocalDataSource,
    private val appExecutors: AppExecutors
): UserRepository {

    override fun getMyProfileFromLocal(): Flow<Resource<User>> =
        local.selectUser().map { Resource.Success(it.toModel()) }

    override fun getMyProfileFromInternet(): Flow<Resource<User>> =
        flow {
            emit(Resource.Loading())
            when (val response = remote.getMyProfile().first()) {
                is Response.Success -> {
                    emit(Resource.Success(response.data.toModel()))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
                else -> {}
            }
        }

    override fun updateMyProfile(data: MutableMap<String, Any>): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading())
            when (val response = remote.updateMyProfile(data).first()) {
                is Response.Success -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
                else -> {}
            }
        }

    override fun updateUserLocation(uid: String, latitude: Double, longitude: Double) =
        appExecutors.diskIO().execute {
            local.updateUserLocation(uid, latitude, longitude)
        }

    override fun logout() = remote.logout()

    override fun setFcmToken(token: String) {
        local.setFcmToken(token)
    }

    override fun getFcmToken(): String =
        local.getFcmToken()!!

    override fun updatePassword(oldPassword: String, newPassword: String): Flow<Resource<UserResponse>> =
        flow {
            emit(Resource.Loading())
            when (val response = remote.updatePassword(oldPassword, newPassword).first()) {
                is Response.Success -> {
                    emit(Resource.Success(response.data))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
                is Response.Empty -> {
                    emit(Resource.Success(null))
                }
            }
        }

}