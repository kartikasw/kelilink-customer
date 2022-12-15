package com.example.kelilink.core.data.helper

import com.example.kelilink.core.domain.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

abstract class NetworkBoundRequest<RequestType> {
    private val result: Flow<Resource<Unit>> = flow {
        preRequest()
        emit(Resource.Loading())
        when (val firebaseResponse = createCall().first()) {
            is Response.Success<RequestType> -> {
                saveCallResult(firebaseResponse.data)
                emit(Resource.Success(null))
            }
            is Response.Error -> {
                emit(
                    Resource.Error<Unit>(
                        firebaseResponse.errorMessage
                    )
                )
            }
            is Response.Empty -> { }
        }
    }

    protected open suspend fun preRequest(){}

    protected abstract suspend fun createCall(): Flow<Response<RequestType>>

    protected abstract suspend fun saveCallResult(data: RequestType)

    fun asFlow() = result
}