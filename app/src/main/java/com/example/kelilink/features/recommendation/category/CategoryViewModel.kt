package com.example.kelilink.features.recommendation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kelilink.core.domain.use_case.recommendation.RecommendationUseCase
import com.example.kelilink.core.domain.use_case.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val recommendationUseCase: RecommendationUseCase,
    private val userUseCase: UserUseCase
): ViewModel() {

    fun getStoreByCategory(category: String) =
        recommendationUseCase.getStoreByCategory(category).asLiveData()

    fun getMyProfile() =
        userUseCase.getMyProfileFromLocal()

    fun refreshAllStore(category: String) =
        recommendationUseCase.getStoreByCategory(category).asLiveData()

}