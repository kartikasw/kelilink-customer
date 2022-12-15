package com.example.kelilink.core.domain.model

data class Order(
    val amount: Int,
    val name: String,
    val note: String,
    val price: Int,
    val total_price: Int,
    val unit: String
)