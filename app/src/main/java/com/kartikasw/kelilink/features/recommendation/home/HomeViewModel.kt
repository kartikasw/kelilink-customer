package com.kartikasw.kelilink.features.recommendation.home

import android.location.Location
import androidx.lifecycle.*
import com.kartikasw.kelilink.core.domain.use_case.recommendation.RecommendationUseCase
import com.kartikasw.kelilink.core.domain.use_case.user.UserUseCase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val recommendationUseCase: RecommendationUseCase,
    private val userUseCase: UserUseCase
): ViewModel() {

    private val _location = MutableLiveData<Pair<String, Location>>()
    val location: LiveData<Pair<String, Location>> = _location

    private val _showProgress = MutableLiveData<Boolean>()
    val showProgress: LiveData<Boolean> = _showProgress

    private suspend fun getCurrentLocation() =
        recommendationUseCase.getCurrentLocation()

    private suspend fun getAddressFromLocation(location: Location) =
        recommendationUseCase.getAddressFromLocation(location)

    fun getCurrentAddress() {
        viewModelScope.launch {
            _showProgress.value = true
            val currentLocation = getCurrentLocation()
            if(currentLocation != null) {
                val uid = Firebase.auth.currentUser!!.uid
                var returnFormLocation = getAddressFromLocation(currentLocation)
                var fromLocation: String
                if(returnFormLocation.isNotEmpty()) {
                    fromLocation = (returnFormLocation)[0]
                } else {
                    returnFormLocation = getAddressFromLocation(currentLocation)
                    fromLocation = returnFormLocation[0]
                }
                updateUserLocation(uid, currentLocation.latitude, currentLocation.longitude)
                _location.value = Pair(fromLocation, currentLocation)
                _showProgress.value = false
            } else {
                _showProgress.value = true
            }

        }
    }

    private fun updateUserLocation(uid: String, latitude: Double, longitude: Double) =
        userUseCase.updateUserLocation(uid, latitude, longitude)

    fun getAllStore() =
        recommendationUseCase.getAllStore().asLiveData()

    fun getMyProfile() =
        userUseCase.getMyProfileFromLocal().asLiveData()

    fun refreshAllStore() =
        recommendationUseCase.getAllStore().asLiveData()


}