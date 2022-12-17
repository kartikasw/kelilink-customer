package com.kartikasw.kelilink.core.data.mapper

import com.kartikasw.kelilink.core.data.source.local.room.entity.MenuEntity
import com.kartikasw.kelilink.core.data.source.remote.response.MenuResponse
import com.kartikasw.kelilink.core.domain.model.Menu
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun MenuResponse.toEntity(): MenuEntity =
    MenuEntity(
        amount = 0,
        available, description, id, image, name,
        note = "",
        price, store_id,
        total_price = 0,
        unit
    )

fun MenuEntity.toModel(): Menu =
    Menu(
        amount, available, description, id, image, name, note, price, store_id, total_price, unit
    )

fun List<MenuEntity>.toListModel(): List<Menu> =
    this.map { it.toModel() }

fun List<MenuResponse>.toListEntity(): List<MenuEntity> =
    this.map { it.toEntity() }

fun Flow<List<MenuEntity>>.toListFlowModel(): Flow<List<Menu>> =
    this.map { it.toListModel() }