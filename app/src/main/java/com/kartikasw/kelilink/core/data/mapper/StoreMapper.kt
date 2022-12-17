package com.kartikasw.kelilink.core.data.mapper

import com.kartikasw.kelilink.core.data.source.remote.response.StoreResponse
import com.kartikasw.kelilink.core.domain.model.Store

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
