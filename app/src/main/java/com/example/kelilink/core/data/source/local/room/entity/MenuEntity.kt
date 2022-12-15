package com.example.kelilink.core.data.source.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kelilink.core.data.helper.Constants.DatabaseColumn.AMOUNT_COLUMN
import com.example.kelilink.core.data.helper.Constants.DatabaseColumn.AVAILABLE_COLUMN
import com.example.kelilink.core.data.helper.Constants.DatabaseColumn.DESCRIPTION_COLUMN
import com.example.kelilink.core.data.helper.Constants.DatabaseColumn.IMAGE_COLUMN
import com.example.kelilink.core.data.helper.Constants.DatabaseColumn.NAME_COLUMN
import com.example.kelilink.core.data.helper.Constants.DatabaseColumn.NOTE_COLUMN
import com.example.kelilink.core.data.helper.Constants.DatabaseColumn.PRICE_COLUMN
import com.example.kelilink.core.data.helper.Constants.DatabaseColumn.STORE_ID_COLUMN
import com.example.kelilink.core.data.helper.Constants.DatabaseColumn.TOTAL_PRICE_COLUMN
import com.example.kelilink.core.data.helper.Constants.DatabaseColumn.UNIT_COLUMN
import com.example.kelilink.core.data.helper.Constants.Table.MENU_TABLE

@Entity(tableName = MENU_TABLE)
data class MenuEntity(

    @ColumnInfo(name = AMOUNT_COLUMN)
    val amount: Int = 0,

    @ColumnInfo(name = AVAILABLE_COLUMN)
    val available: Boolean = false,

    @ColumnInfo(name = DESCRIPTION_COLUMN)
    val description: String = "",

    @PrimaryKey
    val id: String = "",

    @ColumnInfo(name = IMAGE_COLUMN)
    val image: String = "",

    @ColumnInfo(name = NAME_COLUMN)
    val name: String = "",

    @ColumnInfo(name = NOTE_COLUMN)
    val note: String = "",

    @ColumnInfo(name = PRICE_COLUMN)
    val price: Int = 0,

    @ColumnInfo(name = STORE_ID_COLUMN)
    val store_id: String = "",

    @ColumnInfo(name = TOTAL_PRICE_COLUMN)
    val total_price: Int = 0,

    @ColumnInfo(name = UNIT_COLUMN)
    val unit: String = ""

)