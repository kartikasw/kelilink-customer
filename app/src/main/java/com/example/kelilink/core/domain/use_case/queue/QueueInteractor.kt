package com.example.kelilink.core.domain.use_case.queue

import com.example.kelilink.core.domain.Resource
import com.example.kelilink.core.domain.model.Invoice
import com.example.kelilink.core.domain.model.Order
import com.example.kelilink.core.domain.repository.QueueRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QueueInteractor @Inject constructor(private val queueRepository: QueueRepository):
    QueueUseCase {
    override fun getInvoiceById(invoiceId: String): Flow<Resource<Invoice>> =
        queueRepository.getInvoiceById(invoiceId)

    override fun getInvoiceOrder(invoiceId: String): Flow<Resource<List<Order>>> =
        queueRepository.getInvoiceOrder(invoiceId)

    override fun getAllOrder(): Flow<Resource<List<Invoice>>> =
        queueRepository.getAllOrder()

    override fun getAllInvoice(): Flow<Resource<List<Invoice>>> =
        queueRepository.getAllInvoice()

    override fun updateInvoiceQueueOrder(invoiceId: String, queueOrder: Int): Flow<Resource<Unit>> =
        queueRepository.updateInvoiceQueueOrder(invoiceId, queueOrder)
}