package com.example.kelilink.features.order.store.detail_menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kelilink.core.domain.use_case.order.OrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuDialogViewModel @Inject constructor(
    private val orderUseCase: OrderUseCase,
): ViewModel() {

    fun getMenuById(menuId: String) =
        orderUseCase.getMenuById(menuId).asLiveData()

}