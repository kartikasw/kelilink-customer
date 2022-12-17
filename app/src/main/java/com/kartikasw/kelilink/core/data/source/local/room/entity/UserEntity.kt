package com.kartikasw.kelilink.core.data.source.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kartikasw.kelilink.core.data.helper.Constants.DatabaseColumn.EMAIL_COLUMN
import com.kartikasw.kelilink.core.data.helper.Constants.DatabaseColumn.LATITUDE_COLUMN
import com.kartikasw.kelilink.core.data.helper.Constants.DatabaseColumn.LONGITUDE_COLUMN
import com.kartikasw.kelilink.core.data.helper.Constants.DatabaseColumn.NAME_COLUMN
import com.kartikasw.kelilink.core.data.helper.Constants.DatabaseColumn.PHONE_NUMBER_COLUMN
import com.kartikasw.kelilink.core.data.helper.Constants.DatabaseColumn.UID_COLUMN
import com.kartikasw.kelilink.core.data.helper.Constants.Table.USER_TABLE

@Entity(tableName = USER_TABLE)
data class UserEntity(
    @ColumnInfo(name = EMAIL_COLUMN)
    val email: String = "",

    @ColumnInfo(name = LATITUDE_COLUMN)
    val lat: Double = 0.0,

    @ColumnInfo(name = LONGITUDE_COLUMN)
    val lon: Double = 0.0,

    @ColumnInfo(name = NAME_COLUMN)
    val name: String = "",

    @ColumnInfo(name = PHONE_NUMBER_COLUMN)
    val phone_number: String = "",

    @PrimaryKey
    @ColumnInfo(name = UID_COLUMN)
    val uid: String = "",
)