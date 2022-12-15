package com.example.kelilink.core.data.mapper

import com.example.kelilink.core.data.source.remote.response.InvoiceResponse
import com.example.kelilink.core.domain.model.Invoice

fun InvoiceResponse.toModel(): Invoice =
    Invoice(
        address, id, queue_order, status, store_id, store_image, store_lat, store_lon, store_name, time, time_expire, total_price, user_fcm_token, user_id
    )

fun List<InvoiceResponse>.toListModel(): List<Invoice> =
    this.map { it.toModel() }