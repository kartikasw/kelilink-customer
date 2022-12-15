package com.example.kelilink.core.domain.model

data class Fcm(
    val to: String = "",
    val data: FcmData
)

data class FcmData(
    val invoice_id: String = ""
)