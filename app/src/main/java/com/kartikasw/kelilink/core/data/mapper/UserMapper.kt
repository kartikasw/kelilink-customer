package com.kartikasw.kelilink.core.data.mapper

import com.kartikasw.kelilink.core.data.source.local.room.entity.UserEntity
import com.kartikasw.kelilink.core.data.source.remote.response.UserResponse
import com.kartikasw.kelilink.core.domain.model.User

fun UserResponse.toEntity(): UserEntity =
    UserEntity(
        email,
        lat = 0.0,
        lon = 0.0,
        name, phone_number, uid
    )

fun UserEntity.toModel(): User =
    User(
        email, lat, lon, name, phone_number, uid
    )

fun UserResponse.toModel(): User =
    User(
        email,
        lat = 0.0,
        lon = 0.0,
        name, phone_number, uid
    )