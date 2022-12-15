package com.example.kelilink.features.order.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kelilink.core.domain.Resource
import com.example.kelilink.core.domain.model.Menu
import com.example.kelilink.core.domain.model.Store
import com.example.kelilink.core.domain.use_case.order.OrderUseCase
import com.example.kelilink.core.domain.use_case.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val orderUseCase: OrderUseCase,
    private val userUseCase: UserUseCase
): ViewModel() {

    private val _totalPrice = MutableLiveData<Int>()
    val totalPrice: LiveData<Int> = _totalPrice

    private val _totalItem = MutableLiveData<Int>()
    val totalItem: LiveData<Int> = _totalItem

    init {
        _totalItem.value = 0
        _totalPrice.value = 0
    }

    fun updatePrice(price: Int) {
        _totalPrice.value = _totalPrice.value?.plus(price)
    }

    fun updateItem(item: Int) {
        _totalItem.value = _totalItem.value?.plus(item)
    }

    fun getStoreById(id: String): LiveData<Resource<Store>> =
        orderUseCase.getStoreById(id).asLiveData()

    fun getMenuByStoreId(storeId: String): LiveData<Resource<List<Menu>>> =
        orderUseCase.getMenuByStoreId(storeId).asLiveData()

    fun updateAmountAndTotalPriceMenu(id: String, amount: Int, price: Int) {
        orderUseCase.updateAmountAndTotalPriceMenu(id, amount, price)
    }

    fun updateNoteMenu(id: String, note: String) {
        orderUseCase.updateNoteMenu(id, note)
    }

    fun deleteAllMenu() {
        orderUseCase.deleteAllMenu()
    }

    fun getMyProfile() =
        userUseCase.getMyProfileFromLocal()
}