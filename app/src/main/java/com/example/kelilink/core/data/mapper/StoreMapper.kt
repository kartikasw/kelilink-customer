package com.example.kelilink.core.data.mapper

import com.example.kelilink.core.data.source.remote.response.StoreResponse
import com.example.kelilink.core.domain.model.Store

fun StoreResponse.toModel(): Store =
    Store(
        address, categories,
        distance = 0.0,
        fcm_token = fcm_token,
        id,
        image, lat, lon, name, operating_status, queue
    )

fun List<StoreResponse>.toListModel(): List<Store> =
    this.map { it.toModel() }
