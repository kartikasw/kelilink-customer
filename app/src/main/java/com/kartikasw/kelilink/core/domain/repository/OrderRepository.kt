package com.kartikasw.kelilink.core.domain.repository

import com.kartikasw.kelilink.core.domain.Resource
import com.kartikasw.kelilink.core.domain.model.*
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getStoreById(id: String): Flow<Resource<Store>>
    fun getMenuById(menuId: String): Flow<Resource<Menu>>
    fun getMenuByStoreId(storeId: String): Flow<Resource<List<Menu>>>
    fun getOrderedMenu(): Flow<Resource<List<Menu>>>
    fun updateAmountAndTotalPriceMenu(id: String, amount: Int, price: Int)
    fun updateNoteMenu(id: String, note: String)
    fun deleteAllMenu()

    fun makeOrder(invoice: Invoice, orders: List<Order>): Flow<Resource<Invoice>>
    fun listenToOrder(invoiceId: String, storeId: String): Flow<Resource<Invoice>>
    fun deleteOrder(invoiceId: String): Flow<Resource<Unit>>

    fun sendFcm(data: Fcm): Flow<Resource<Unit>>
}