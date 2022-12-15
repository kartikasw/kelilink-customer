package com.example.kelilink.core.domain.model

data class User(
    val email: String = "",
    val lat: Double,
    val lon: Double,
    val name: String = "",
    val phone_number: String = "",
    val uid: String = "",
)