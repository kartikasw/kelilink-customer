package com.kartikasw.kelilink.features.transaction.invoice.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kartikasw.kelilink.core.domain.use_case.queue.QueueUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailInvoiceViewModel @Inject constructor(
    private val queueUseCase: QueueUseCase
): ViewModel() {

    fun getInvoiceById(invoiceId: String) =
        queueUseCase.getInvoiceById(invoiceId).asLiveData()

    fun getInvoiceOrder(invoiceId: String) =
        queueUseCase.getInvoiceOrder(invoiceId).asLiveData()

}