package com.example.kelilink.features.transaction.queue.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kelilink.core.domain.use_case.order.OrderUseCase
import com.example.kelilink.core.domain.use_case.queue.QueueUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailQueueViewModel @Inject constructor(
    private val orderUseCase: OrderUseCase,
    private val queueUseCase: QueueUseCase
): ViewModel() {

    fun getInvoiceById(invoiceId: String) =
        queueUseCase.getInvoiceById(invoiceId).asLiveData()

    fun getInvoiceOrder(invoiceId: String) =
        queueUseCase.getInvoiceOrder(invoiceId).asLiveData()

    fun getStoreById(storeId: String) =
        orderUseCase.getStoreById(storeId).asLiveData()

    fun updateQueueOrder(invoiceId: String, queueOrder: Int) =
        queueUseCase.updateInvoiceQueueOrder(invoiceId, queueOrder).asLiveData()

}