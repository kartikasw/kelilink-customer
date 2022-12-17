package com.kartikasw.kelilink.core.domain.model

data class Menu(
    val amount: Int,
    val available: Boolean,
    val description: String = "",
    val id: String,
    val image: String,
    val name: String,
    val note: String,
    val price: Int,
    val store_id: String,
    val total_price: Int,
    val unit: String
)
