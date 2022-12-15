package com.example.kelilink.core.data.source.remote.response

data class FcmResponse(
    val success: Int = 0,
    val results: List<FcmErrorResponse> = listOf()
)

data class FcmErrorResponse(
    val error: String = ""
)