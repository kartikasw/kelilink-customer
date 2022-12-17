package com.kartikasw.kelilink.core.data.repository

import android.location.Location
import com.kartikasw.kelilink.core.data.helper.Response
import com.kartikasw.kelilink.core.data.mapper.toListModel
import com.kartikasw.kelilink.core.data.source.remote.RemoteDataSource
import com.kartikasw.kelilink.core.domain.Resource
import com.kartikasw.kelilink.core.domain.model.Store
import com.kartikasw.kelilink.core.domain.repository.RecommendationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RecommendationRepositoryImpl @Inject constructor(
    private val remote: RemoteDataSource
): RecommendationRepository {

    override fun getAllStore(): Flow<Resource<List<Store>>> =
        flow {
            emit(Resource.Loading())
            when(val response = remote.getAllStore().first()) {
                is Response.Success -> {
                    emit(Resource.Success(response.data.toListModel()))
                }
                is Response.Empty -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
            }
        }

    override fun getStoreByCategory(category: String): Flow<Resource<List<Store>>> =
        flow {
            emit(Resource.Loading())
            when(val response = remote.getStoreByCategory(category).first()) {
                is Response.Success -> {
                    emit(Resource.Success(response.data.toListModel()))
                }
                is Response.Empty -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
            }
        }

    override suspend fun getCurrentLocation(): Location? =
        remote.getCurrentLocation()

    override suspend fun getAddressFromLocation(location: Location): List<String> =
        remote.getAddressFromLocation(location)
}