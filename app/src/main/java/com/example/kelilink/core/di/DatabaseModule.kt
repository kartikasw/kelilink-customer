package com.example.kelilink.core.di

import android.content.Context
import androidx.room.Room
import com.example.kelilink.core.data.helper.Constants.DATABASE_NAME
import com.example.kelilink.core.data.source.local.room.KelilinkDatabase
import com.example.kelilink.core.data.source.local.room.dao.StoreDao
import com.example.kelilink.core.data.source.local.room.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): KelilinkDatabase =
        Room.databaseBuilder(
            context,
            KelilinkDatabase::class.java, DATABASE_NAME
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideStoreDao(database: KelilinkDatabase): StoreDao =
        database.storeDao()

    @Provides
    fun provideUserDao(database: KelilinkDatabase): UserDao =
        database.userDao()

}