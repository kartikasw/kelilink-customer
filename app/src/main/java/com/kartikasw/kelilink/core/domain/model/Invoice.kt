package com.kartikasw.kelilink.core.domain.model

import java.util.*

data class Invoice(
    val address: String = "",
    val id: String = "",
    val queue_order: Int = 0,
    val status: String = "",
    val store_id: String = "",
    val store_image: String = "",
    val store_lat: Double = 0.0,
    val store_lon: Double = 0.0,
    val store_name: String = "",
    val time: Date = Date(0),
    val time_expire: Date = Date(0),
    val total_price: Int = 0,
    val user_fcm_token: String = "",
    val user_id: String = "",
    val user_phone_number: String = ""
)