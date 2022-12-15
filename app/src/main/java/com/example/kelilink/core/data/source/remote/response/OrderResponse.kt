package com.example.kelilink.core.data.source.remote.response

data class OrderResponse(
    val amount: Int = 0,
    val name: String = "",
    val note: String = "",
    val price: Int = 0,
    val total_price: Int = 0,
    val unit: String = ""
)