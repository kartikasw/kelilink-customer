package com.kartikasw.kelilink.core.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kartikasw.kelilink.core.data.helper.Constants.Table.MENU_TABLE
import com.kartikasw.kelilink.core.data.source.local.room.entity.MenuEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreDao {

    @Query("SELECT * from $MENU_TABLE")
    fun selectAllMenu(): Flow<List<MenuEntity>>

    @Query("SELECT * from $MENU_TABLE WHERE id = :menuId")
    fun selectMenuById(menuId: String): Flow<MenuEntity>

    @Query("SELECT * from $MENU_TABLE WHERE amount > 0")
    fun selectOrderedMenu(): Flow<List<MenuEntity>>

    @Query("DELETE FROM $MENU_TABLE")
    fun deleteAllMenu()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMenu(menuEntity: List<MenuEntity>)

    @Query("UPDATE $MENU_TABLE SET amount = :amount, total_price = :price WHERE id = :id")
    fun updateAmountAndTotalPriceMenu(id: String, amount: Int, price: Int)

    @Query("UPDATE $MENU_TABLE SET note = :note WHERE id = :id")
    fun updateNoteMenu(id: String, note: String)

}