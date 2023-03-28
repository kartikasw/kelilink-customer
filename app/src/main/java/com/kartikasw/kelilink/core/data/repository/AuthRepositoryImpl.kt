package com.kartikasw.kelilink.core.data.repository

import com.kartikasw.kelilink.core.data.helper.NetworkBoundRequest
import com.kartikasw.kelilink.core.data.helper.Response
import com.kartikasw.kelilink.core.data.mapper.toEntity
import com.kartikasw.kelilink.core.data.source.local.LocalDataSource
import com.kartikasw.kelilink.core.data.source.remote.RemoteDataSource
import com.kartikasw.kelilink.core.data.source.remote.response.UserResponse
import com.kartikasw.kelilink.core.domain.Resource
import com.kartikasw.kelilink.core.domain.repository.AuthRepository
import com.kartikasw.kelilink.util.params.RegisterParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val remote: RemoteDataSource,
    private val local: LocalDataSource
): AuthRepository {

    override fun register(param: RegisterParam): Flow<Resource<Unit>> =
        object : NetworkBoundRequest<UserResponse>() {

            override suspend fun createCall(): Flow<Response<UserResponse>> =
                remote.register(param)

            override suspend fun saveCallResult(data: UserResponse) {
                local.deleteUser()
                local.insertUser(data.toEntity())
            }

        }.asFlow()

    override fun logIn(email: String, password: String, fcmToken: String): Flow<Resource<Unit>> =
        object : NetworkBoundRequest<UserResponse>() {

            override suspend fun createCall(): Flow<Response<UserResponse>> =
                remote.logIn(email, password, fcmToken)

            override suspend fun saveCallResult(data: UserResponse) {
                local.deleteUser()
                local.insertUser(data.toEntity())
            }

        }.asFlow()

    override fun resetPassword(email: String): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading())
            when(val response = remote.resetPassword(email).first()) {
                is Response.Success -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
                else -> {}
            }
        }
}