package com.kartikasw.kelilink.core.data.source.remote.service.firebase

import com.kartikasw.kelilink.core.data.helper.Constants.DatabaseCollection.INVOICE_COLLECTION
import com.kartikasw.kelilink.core.data.helper.Constants.DatabaseCollection.ORDERS_COLLECTION
import com.kartikasw.kelilink.core.data.helper.Constants.DatabaseColumn.QUEUE_ORDER_COLUMN
import com.kartikasw.kelilink.core.data.helper.Constants.DatabaseColumn.USER_ID_COLUMN
import com.kartikasw.kelilink.core.data.helper.Response
import com.kartikasw.kelilink.core.data.source.remote.response.InvoiceResponse
import com.kartikasw.kelilink.core.data.source.remote.response.OrderResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class QueueService @Inject constructor(): FirebaseService() {

    fun getInvoiceById(invoiceId: String): Flow<Response<InvoiceResponse>> =
        getDocumentById<InvoiceResponse>(INVOICE_COLLECTION, invoiceId)

    fun getInvoiceOrder(invoiceId: String): Flow<Response<List<OrderResponse>>> =
        getDocumentInSubCollection(INVOICE_COLLECTION, ORDERS_COLLECTION, invoiceId)

    fun getAllOrder(): Flow<Response<List<InvoiceResponse>>> =
        flow {
            val userUid = getUser()!!.uid
            emitAll(getDocumentByFieldAndOrderByTime(INVOICE_COLLECTION, USER_ID_COLUMN, userUid))
        }

    fun getAllInvoice(): Flow<Response<List<InvoiceResponse>>> =
        flow {
            val userUid = getUser()!!.uid
            emitAll(getDocumentByFieldAndOrderByTime(INVOICE_COLLECTION, USER_ID_COLUMN, userUid))
        }

    fun updateInvoiceQueueOrder(invoiceId: String, queueOrder: Int): Flow<Response<InvoiceResponse>> =
        updateFieldInDocument(INVOICE_COLLECTION, invoiceId, QUEUE_ORDER_COLUMN, queueOrder)
}