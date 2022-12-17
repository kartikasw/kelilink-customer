package com.kartikasw.kelilink.features.order.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kartikasw.kelilink.core.domain.Resource
import com.kartikasw.kelilink.core.domain.model.Fcm
import com.kartikasw.kelilink.core.domain.model.Invoice
import com.kartikasw.kelilink.core.domain.model.Menu
import com.kartikasw.kelilink.core.domain.model.Order
import com.kartikasw.kelilink.core.domain.use_case.order.OrderUseCase
import com.kartikasw.kelilink.core.domain.use_case.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderUseCase: OrderUseCase,
    private val userUseCase: UserUseCase
): ViewModel() {

    fun getStoreById(id: String) =
        orderUseCase.getStoreById(id).asLiveData()

    fun getOrderedMenu(): LiveData<Resource<List<Menu>>> =
        orderUseCase.getOrderedMenu().asLiveData()

    fun getMyProfile() =
        userUseCase.getMyProfileFromLocal()

    fun makeOrder(invoice: Invoice, orders: List<Order>) =
        orderUseCase.makeOrder(invoice, orders).asLiveData()

    fun sendFcm(data: Fcm) =
        orderUseCase.sendFcm(data).asLiveData()

    fun getFcmToken() =
        userUseCase.getFcmToken()

}