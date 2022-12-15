package com.example.kelilink.core.data.source.remote.response

data class UserResponse(
    val email: String = "",
    val fcm_token: String = "",
    val name: String = "",
    val phone_number: String = "",
    val uid: String = "",
)