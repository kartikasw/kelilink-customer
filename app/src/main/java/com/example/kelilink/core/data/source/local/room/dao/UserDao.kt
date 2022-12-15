package com.example.kelilink.core.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kelilink.core.data.helper.Constants.DatabaseColumn.LATITUDE_COLUMN
import com.example.kelilink.core.data.helper.Constants.DatabaseColumn.LONGITUDE_COLUMN
import com.example.kelilink.core.data.helper.Constants.DatabaseColumn.UID_COLUMN
import com.example.kelilink.core.data.helper.Constants.Table.USER_TABLE
import com.example.kelilink.core.data.source.local.room.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * from $USER_TABLE")
    fun selectUser(): Flow<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("UPDATE $USER_TABLE SET $LATITUDE_COLUMN = :latitude, $LONGITUDE_COLUMN = :longitude " +
            "WHERE $UID_COLUMN = :uid")
    fun updateUserLocation(uid: String, latitude: Double, longitude: Double)

    @Query("DELETE FROM $USER_TABLE")
    suspend fun delete()
}