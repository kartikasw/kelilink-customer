package com.example.kelilink.core.data.repository

import com.example.kelilink.core.data.helper.NetworkBoundRequest
import com.example.kelilink.core.data.helper.Response
import com.example.kelilink.core.data.mapper.toEntity
import com.example.kelilink.core.data.source.local.LocalDataSource
import com.example.kelilink.core.data.source.remote.RemoteDataSource
import com.example.kelilink.core.data.source.remote.response.UserResponse
import com.example.kelilink.core.domain.Resource
import com.example.kelilink.core.domain.repository.AuthRepository
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

    override fun register(email: String, password: String, user: MutableMap<String, Any>): Flow<Resource<Unit>> =
        object : NetworkBoundRequest<UserResponse>() {

            override suspend fun createCall(): Flow<Response<UserResponse>> =
                remote.register(email, password, user)

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