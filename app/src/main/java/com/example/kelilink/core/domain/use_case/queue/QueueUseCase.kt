package com.example.kelilink.core.domain.use_case.queue

import com.example.kelilink.core.domain.Resource
import com.example.kelilink.core.domain.model.Invoice
import com.example.kelilink.core.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface QueueUseCase {
    fun getInvoiceById(invoiceId: String): Flow<Resource<Invoice>>
    fun getInvoiceOrder(invoiceId: String): Flow<Resource<List<Order>>>

    fun getAllOrder(): Flow<Resource<List<Invoice>>>
    fun getAllInvoice(): Flow<Resource<List<Invoice>>>

    fun updateInvoiceQueueOrder(invoiceId: String, queueOrder: Int): Flow<Resource<Unit>>
}