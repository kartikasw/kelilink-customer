package com.example.kelilink.features.order.order.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kelilink.core.domain.use_case.order.OrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderDialogViewModel @Inject constructor(
    private val orderUseCase: OrderUseCase,
): ViewModel() {
    fun listenToOrder(invoiceId: String, storeId: String) =
        orderUseCase.listenToOrder(invoiceId, storeId).asLiveData()

    fun deleteOrder(invoiceId: String) =
        orderUseCase.deleteOrder(invoiceId).asLiveData()
}