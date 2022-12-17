package com.kartikasw.kelilink.core.data.source.remote.service.location

import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LocationApi @Inject constructor(
    private val locationProvider: FusedLocationProviderClient
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getCurrentLocation(): Location? {
        val cancellationTokenSource = CancellationTokenSource()
        return try {
            locationProvider.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            ).await(cancellationTokenSource)
        } catch (e: SecurityException) {
            Log.w("LocationApi", "Couldn't get location, did you request location permissions?", e)
            null
        }
    }

}
