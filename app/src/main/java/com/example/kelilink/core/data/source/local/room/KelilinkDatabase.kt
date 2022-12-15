package com.example.kelilink.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.kelilink.core.data.source.local.room.converter.ListConverter
import com.example.kelilink.core.data.source.local.room.dao.StoreDao
import com.example.kelilink.core.data.source.local.room.dao.UserDao
import com.example.kelilink.core.data.source.local.room.entity.MenuEntity
import com.example.kelilink.core.data.source.local.room.entity.UserEntity

@Database(
    entities = [MenuEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ListConverter::class)
abstract class KelilinkDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun storeDao(): StoreDao

}