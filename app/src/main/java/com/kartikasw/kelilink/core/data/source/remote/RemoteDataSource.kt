package com.kartikasw.kelilink.core.data.source.remote

import android.location.Location
import com.kartikasw.kelilink.core.data.helper.Response
import com.kartikasw.kelilink.core.data.source.remote.response.FcmResponse
import com.kartikasw.kelilink.core.data.source.remote.service.firebase.AuthService
import com.kartikasw.kelilink.core.data.source.remote.service.firebase.QueueService
import com.kartikasw.kelilink.core.data.source.remote.service.firebase.StoreService
import com.kartikasw.kelilink.core.data.source.remote.service.firebase.UserService
import com.kartikasw.kelilink.core.data.source.remote.service.firebase.notification.ApiService
import com.kartikasw.kelilink.core.data.source.remote.service.location.GeocodingApi
import com.kartikasw.kelilink.core.data.source.remote.service.location.LocationApi
import com.kartikasw.kelilink.core.domain.model.Fcm
import com.kartikasw.kelilink.core.domain.model.Invoice
import com.kartikasw.kelilink.core.domain.model.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val api: ApiService,
    private val auth: AuthService,
    private val queue: QueueService,
    private val store: StoreService,
    private val user: UserService,
    private val geocoding: GeocodingApi,
    private val location: LocationApi,
) {

    suspend fun getCurrentLocation() =
        location.getCurrentLocation()

    suspend fun getAddressFromLocation(location: Location) =
        geocoding.getFromLocation(location)

    suspend fun sendFcm(data: Fcm): Flow<Response<FcmResponse>> =
        flow<Response<FcmResponse>> {
            val response = api.sendFcm(data)
            if(response.success == 1) {
                emit(Response.Success(response))
            } else {
                emit(Response.Error(response.results[0].error))
            }
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    /*
    *
    * AUTH
    *
     */
    fun register(email: String, password: String, user: MutableMap<String, Any>) =
        auth.register(email, password, user)

    fun logIn(email: String, password: String, fcmToken: String) =
        auth.logIn(email, password, fcmToken)

    fun resetPassword(email: String) =
        auth.resetPassword(email)

    /*
    *
    * QUEUE
    *
     */
    fun getInvoiceById(invoiceId: String) =
        queue.getInvoiceById(invoiceId)

    fun getInvoiceOrder(invoiceId: String) =
        queue.getInvoiceOrder(invoiceId)

    fun getAllOrder() =
        queue.getAllOrder()

    fun getAllInvoice() =
        queue.getAllInvoice()

    fun updateInvoiceQueueOrder(invoiceId: String, queueOrder: Int) =
        queue.updateInvoiceQueueOrder(invoiceId, queueOrder)

    /*
    *
    * USER
    *
     */
    fun getMyProfile() =
        user.getMyProfile()

    fun updateMyProfile(data: MutableMap<String, Any>) =
        user.updateMyProfile(data)

    fun updatePassword(oldPassword: String, newPassword: String) =
        user.updatePassword(oldPassword, newPassword)

    fun logout() =
        user.logout()

    /*
    *
    * STORE
    *
     */
    fun getAllStore() =
        store.getAllStore()

    fun getStoreById(id: String) =
        store.getStoreById(id)

    fun getStoreByCategory(category: String) =
        store.getStoreByCategory(category)

    fun getMenuByStoreId(storeId: String) =
        store.getMenuByStoreId(storeId)

    fun makeOrder(invoice: Invoice, orders: List<Order>) =
        store.makeOrder(invoice, orders)

    fun listenToOrder(invoiceId: String, storeId: String) =
        store.listenToOrder(invoiceId, storeId)

    fun deleteOrder(invoiceId: String) =
        store.deleteOrder(invoiceId)
}