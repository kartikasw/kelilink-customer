package com.example.kelilink.core.data.helper

import com.example.kelilink.core.domain.Resource
import kotlinx.coroutines.flow.*

abstract class NetworkBoundResource<ResultType, RequestType> {

    private val result: Flow<Resource<ResultType>> = flow {
        val dbSource = loadFromDB().first()
        emit(Resource.Loading(dbSource))
        if (shouldFetch(dbSource)) {
            when (val response = createCall().first()) {
                is Response.Success<RequestType> -> {
                    saveCallResult(response.data)
                    emitAll(loadFromDB().map {
                        Resource.Success(
                            it
                        )
                    })
                }
                is Response.Empty -> {
                    emitAll(loadFromDB().map {
                        Resource.Success(
                            it
                        )
                    })
                }
                is Response.Error -> {
                    emit(
                        Resource.Error<ResultType>(
                            response.errorMessage
                        )
                    )
                }
            }
        } else {
            emitAll(loadFromDB().map {
                Resource.Success(it)
            })
        }
    }

    protected abstract fun loadFromDB(): Flow<ResultType?>

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract suspend fun createCall(): Flow<Response<RequestType>>

    protected abstract suspend fun saveCallResult(data: RequestType)

    fun asFlow() = result

}