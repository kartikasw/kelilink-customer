package com.example.kelilink.core.data.source.remote.service.firebase

import com.example.kelilink.core.data.helper.Constants.DatabaseCollection.INVOICE_COLLECTION
import com.example.kelilink.core.data.helper.Constants.DatabaseCollection.MENU_COLLECTION
import com.example.kelilink.core.data.helper.Constants.DatabaseCollection.ORDERS_COLLECTION
import com.example.kelilink.core.data.helper.Constants.DatabaseCollection.STORE_COLLECTION
import com.example.kelilink.core.data.helper.Constants.DatabaseColumn.CATEGORIES_COLUMN
import com.example.kelilink.core.data.helper.Constants.DatabaseColumn.OPERATING_STATUS_COLUMN
import com.example.kelilink.core.data.helper.Constants.DatabaseColumn.QUEUE_COLUMN
import com.example.kelilink.core.data.helper.Constants.DatabaseColumn.STATUS_COLUMN
import com.example.kelilink.core.data.helper.Constants.DatabaseColumn.STORE_ID_COLUMN
import com.example.kelilink.core.data.helper.Constants.InvoiceStatus.COOKING
import com.example.kelilink.core.data.helper.Response
import com.example.kelilink.core.data.source.remote.response.InvoiceResponse
import com.example.kelilink.core.data.source.remote.response.MenuResponse
import com.example.kelilink.core.data.source.remote.response.StoreResponse
import com.example.kelilink.core.domain.model.Invoice
import com.example.kelilink.core.domain.model.Order
import com.google.firebase.firestore.MetadataChanges
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StoreService @Inject constructor(): FirebaseService() {

    fun getAllStore(): Flow<Response<List<StoreResponse>>> =
        getDocumentByField(STORE_COLLECTION, OPERATING_STATUS_COLUMN, true)

    fun getStoreById(id: String): Flow<Response<StoreResponse>> =
        getDocumentById(STORE_COLLECTION, id)

    fun getStoreByCategory(category: String): Flow<Response<List<StoreResponse>>> =
        getDocumentByArrayValue(STORE_COLLECTION, CATEGORIES_COLUMN, category)

    fun getMenuByStoreId(storeId: String): Flow<Response<List<MenuResponse>>> =
        getDocumentByField(MENU_COLLECTION, STORE_ID_COLUMN, storeId)

    fun makeOrder(invoice: Invoice, orders: List<Order>): Flow<Response<InvoiceResponse>> =
        flow {
            addDocument<InvoiceResponse>(
                INVOICE_COLLECTION,
                invoice
            ).collect {
                when(it) {
                    is Response.Success -> {
                        emitAll(
                            setDocumentInSubCollection(
                                INVOICE_COLLECTION,
                                it.data.id,
                                ORDERS_COLLECTION,
                                orders
                            )
                        )
                    }
                    is Response.Error -> {
                        emit(Response.Error(it.errorMessage))
                    }
                    is Response.Empty -> {
                        emit(Response.Empty)
                    }
                }
            }
        }

    fun listenToOrder(invoiceId: String, storeId: String): Flow<Response<InvoiceResponse>> =
        flow {
            val ref = firestore
                .collection(INVOICE_COLLECTION)
                .document(invoiceId)

            var isAccepted = false

            val listener = ref.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    if(snapshot.data?.get(STATUS_COLUMN) == COOKING) {
                        isAccepted = true
                    }
                }
            }

            delay(25000)

            listener.remove()

            if(isAccepted) {
                addValueToArrayOfDocument(STORE_COLLECTION, storeId, QUEUE_COLUMN, invoiceId).collect {
                    when(it) {
                        is Response.Success -> {
                            emitAll(getDocumentById(INVOICE_COLLECTION, invoiceId))
                        }
                        is Response.Error -> {
                            emit(Response.Error(it.errorMessage))
                        }
                        else -> {}
                    }
                }
            } else {
                emitAll(getDocumentById(INVOICE_COLLECTION, invoiceId))
            }
        }

    fun deleteOrder(invoiceId: String): Flow<Response<Unit>> =
        deleteDocument(INVOICE_COLLECTION, invoiceId)
}