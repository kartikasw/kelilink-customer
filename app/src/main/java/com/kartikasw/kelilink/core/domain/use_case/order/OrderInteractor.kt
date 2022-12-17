package com.kartikasw.kelilink.core.domain.use_case.order

import com.kartikasw.kelilink.core.domain.Resource
import com.kartikasw.kelilink.core.domain.model.*
import com.kartikasw.kelilink.core.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderInteractor @Inject constructor(
    private val orderRepository: OrderRepository
): OrderUseCase {
    override fun getStoreById(id: String): Flow<Resource<Store>> =
        orderRepository.getStoreById(id)

    override fun getMenuById(menuId: String): Flow<Resource<Menu>> =
        orderRepository.getMenuById(menuId)

    override fun getMenuByStoreId(storeId: String): Flow<Resource<List<Menu>>> =
        orderRepository.getMenuByStoreId(storeId)

    override fun getOrderedMenu(): Flow<Resource<List<Menu>>> =
        orderRepository.getOrderedMenu()

    override fun updateAmountAndTotalPriceMenu(id: String, amount: Int, price: Int) =
        orderRepository.updateAmountAndTotalPriceMenu(id, amount, price)

    override fun updateNoteMenu(id: String, note: String) =
        orderRepository.updateNoteMenu(id, note)

    override fun deleteAllMenu() =
        orderRepository.deleteAllMenu()

    override fun makeOrder(invoice: Invoice, orders: List<Order>): Flow<Resource<Invoice>> =
        orderRepository.makeOrder(invoice, orders)

    override fun listenToOrder(invoiceId: String, storeId: String): Flow<Resource<Invoice>> =
        orderRepository.listenToOrder(invoiceId, storeId)

    override fun deleteOrder(invoiceId: String): Flow<Resource<Unit>> =
        orderRepository.deleteOrder(invoiceId)

    override fun sendFcm(data: Fcm): Flow<Resource<Unit>> =
        orderRepository.sendFcm(data)

}