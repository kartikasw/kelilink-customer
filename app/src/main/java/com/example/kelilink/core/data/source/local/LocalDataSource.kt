package com.example.kelilink.core.data.source.local

import com.example.kelilink.core.data.source.local.room.dao.StoreDao
import com.example.kelilink.core.data.source.local.room.dao.UserDao
import com.example.kelilink.core.data.source.local.room.entity.MenuEntity
import com.example.kelilink.core.data.source.local.room.entity.UserEntity
import com.example.kelilink.core.data.source.local.shared_pref.KelilinkPreference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val storeDao: StoreDao,
    private val userDao: UserDao,
    private val pref: KelilinkPreference
) {

    fun setFcmToken(token: String) {
        pref.setFcmToken(token)
    }

    fun getFcmToken() =
        pref.getFcmToken()

    fun selectUser() =
        userDao.selectUser()

    suspend fun insertUser(user: UserEntity) =
        userDao.insertUser(user)

    fun updateUserLocation(uid: String, latitude: Double, longitude: Double) =
        userDao.updateUserLocation(uid, latitude, longitude)

    suspend fun deleteUser() =
        userDao.delete()

    fun selectAllMenu(): Flow<List<MenuEntity>> =
        storeDao.selectAllMenu()

    fun selectMenuById(menuId: String): Flow<MenuEntity> =
        storeDao.selectMenuById(menuId)

    suspend fun insertAllMenu(menu: List<MenuEntity>): Unit =
        storeDao.insertAllMenu(menu)

    fun deleteAllMenu() =
        storeDao.deleteAllMenu()

    fun updateAmountAndTotalPriceMenu(id: String, amount: Int, price: Int){
        storeDao.updateAmountAndTotalPriceMenu(id, amount, price)
    }

    fun updateNoteMenu(id: String, note: String) {
        storeDao.updateNoteMenu(id, note)
    }

    fun selectOrderedMenu(): Flow<List<MenuEntity>> =
        storeDao.selectOrderedMenu()
}