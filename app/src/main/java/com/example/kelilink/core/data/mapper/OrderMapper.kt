package com.example.kelilink.core.data.mapper

import com.example.kelilink.core.data.source.remote.response.OrderResponse
import com.example.kelilink.core.domain.model.Order

fun OrderResponse.toModel() =
    Order(
        amount, name, note, price, total_price, unit
    )

fun List<OrderResponse>.toListModel(): List<Order> =
    this.map { it.toModel() }