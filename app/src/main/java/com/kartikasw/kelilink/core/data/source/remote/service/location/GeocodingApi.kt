package com.kartikasw.kelilink.core.data.source.remote.service.location

import android.location.Geocoder
import android.location.Location
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class GeocodingApi @Inject constructor(
    private val geocoder: Geocoder
) {

    companion object {
        const val TAG = "GeocodingApi"
    }

    suspend fun getFromLocation(
        location: Location,
        maxResults: Int = 1
    ): List<String> = withContext(Dispatchers.IO) {
        try {
            val addresses = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                maxResults
            ) ?: emptyList()
            addresses.map { address ->
                (0..address.maxAddressLineIndex)
                    .joinToString("\n") { address.getAddressLine(it) }
            }
        } catch (e: IOException) {
            Log.w(TAG, "Error trying to get address from location.", e)
            emptyList()
        }
    }
}
