package com.example.kelilink.features.transaction.queue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kelilink.core.domain.use_case.queue.QueueUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QueueViewModel @Inject constructor(
    private val queueUseCase: QueueUseCase
): ViewModel() {

    fun getAllOrder() =
        queueUseCase.getAllOrder().asLiveData()

    fun refreshAllOrder() =
        queueUseCase.getAllOrder().asLiveData()

}