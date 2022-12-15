package com.example.kelilink.features.transaction.invoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kelilink.core.domain.use_case.queue.QueueUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InvoiceViewModel @Inject constructor(
    private val queueUseCase: QueueUseCase
): ViewModel() {

    fun getAllInvoice() =
        queueUseCase.getAllInvoice().asLiveData()

    fun refreshAllInvoice() =
        queueUseCase.getAllInvoice().asLiveData()
}