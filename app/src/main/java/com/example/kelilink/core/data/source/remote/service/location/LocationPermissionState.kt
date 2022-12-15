package com.example.kelilink.core.data.source.remote.service.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

private val locationPermissions =
    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

internal fun Context.hasPermission(permission: String): Boolean =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

internal fun Activity.shouldShowRationaleFor(permission: String): Boolean =
    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)

class LocationPermissionState (
    private val activity: ComponentActivity,
    private val onResult: (LocationPermissionState) -> Unit
) {

    private val _accessCoarseLocationGranted = MutableLiveData<Boolean>()
    val accessCoarseLocationGranted: LiveData<Boolean> = _accessCoarseLocationGranted

    private val _accessCoarseLocationNeedsRationale = MutableLiveData<Boolean>()
    val accessCoarseLocationNeedsRationale: LiveData<Boolean> = _accessCoarseLocationNeedsRationale

    private val _accessFineLocationGranted = MutableLiveData<Boolean>()
    val accessFineLocationGranted: LiveData<Boolean> = _accessFineLocationGranted

    private val _accessFineLocationNeedsRationale = MutableLiveData<Boolean>()
    val accessFineLocationNeedsRationale: LiveData<Boolean> = _accessFineLocationNeedsRationale


    private val _permissionRequested = MutableLiveData<Boolean>()
    val permissionRequested: LiveData<Boolean> = _permissionRequested

    private val permissionLauncher: ActivityResultLauncher<Array<String>> =
        activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            _permissionRequested.value = true
            updateState()
            onResult(this)
        }

    init {
        updateState()
        permissionLauncher
    }

    private fun updateState() {
        _accessCoarseLocationGranted.value = activity.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        _accessCoarseLocationNeedsRationale.value =
            activity.shouldShowRationaleFor(Manifest.permission.ACCESS_COARSE_LOCATION)
        _accessFineLocationGranted.value = activity.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        _accessFineLocationNeedsRationale.value =
            activity.shouldShowRationaleFor(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    fun requestPermissions() {
        permissionLauncher.launch(locationPermissions)
    }

    fun shouldShowRationale(): Boolean {
        return _accessCoarseLocationNeedsRationale.value!! || _accessFineLocationNeedsRationale.value!! ||
                (_permissionRequested.value!! && !_accessFineLocationGranted.value!!)
    }
}