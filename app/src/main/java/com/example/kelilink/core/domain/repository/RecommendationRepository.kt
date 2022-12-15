package com.example.kelilink.core.domain.repository

import android.location.Location
import com.example.kelilink.core.domain.Resource
import com.example.kelilink.core.domain.model.Store
import kotlinx.coroutines.flow.Flow

interface RecommendationRepository {
    fun getAllStore(): Flow<Resource<List<Store>>>
    fun getStoreByCategory(category: String): Flow<Resource<List<Store>>>

    suspend fun getCurrentLocation(): Location?
    suspend fun getAddressFromLocation(location: Location): List<String>
}