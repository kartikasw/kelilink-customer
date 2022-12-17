package com.kartikasw.kelilink.core.domain.model

data class Store(
    val address: String,
    val categories: List<String> = listOf(),
    val distance: Double,
    val fcm_token: String,
    val id: String,
    val image: String,
    val lat: Double,
    val lon: Double,
    val name: String,
    val operating_status: Boolean,
    val queue: List<String>
)
