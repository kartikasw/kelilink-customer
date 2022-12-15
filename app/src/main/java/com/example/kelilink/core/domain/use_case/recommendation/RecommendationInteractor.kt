package com.example.kelilink.core.domain.use_case.recommendation

import android.location.Location
import com.example.kelilink.core.domain.Resource
import com.example.kelilink.core.domain.model.Store
import com.example.kelilink.core.domain.repository.RecommendationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecommendationInteractor @Inject constructor(private val recommendationRepository: RecommendationRepository):
    RecommendationUseCase {
    override fun getAllStore(): Flow<Resource<List<Store>>> =
        recommendationRepository.getAllStore()

    override fun getStoreByCategory(category: String): Flow<Resource<List<Store>>> =
        recommendationRepository.getStoreByCategory(category)

    override suspend fun getCurrentLocation(): Location? =
        recommendationRepository.getCurrentLocation()

    override suspend fun getAddressFromLocation(location: Location): List<String> =
        recommendationRepository.getAddressFromLocation(location)
}