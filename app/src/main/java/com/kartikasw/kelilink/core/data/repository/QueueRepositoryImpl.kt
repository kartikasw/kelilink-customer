package com.kartikasw.kelilink.core.data.repository

import com.kartikasw.kelilink.core.data.helper.Constants.InvoiceStatus.COOKING
import com.kartikasw.kelilink.core.data.helper.Response
import com.kartikasw.kelilink.core.data.mapper.toListModel
import com.kartikasw.kelilink.core.data.mapper.toModel
import com.kartikasw.kelilink.core.data.source.remote.RemoteDataSource
import com.kartikasw.kelilink.core.domain.Resource
import com.kartikasw.kelilink.core.domain.model.Invoice
import com.kartikasw.kelilink.core.domain.model.Order
import com.kartikasw.kelilink.core.domain.repository.QueueRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QueueRepositoryImpl @Inject constructor(
    private val remote: RemoteDataSource
): QueueRepository {
    override fun getInvoiceById(invoiceId: String): Flow<Resource<Invoice>> =
        flow {
            emit(Resource.Loading())
            when(val response = remote.getInvoiceById(invoiceId).first()) {
                is Response.Success -> {
                    emit(Resource.Success(response.data.toModel()))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
                else -> {}
            }
        }

    override fun getInvoiceOrder(invoiceId: String): Flow<Resource<List<Order>>> =
        flow {
            emit(Resource.Loading())
            when(val response = remote.getInvoiceOrder(invoiceId).first()) {
                is Response.Success -> {
                    emit(Resource.Success(response.data.toListModel()))
                }
                is Response.Empty -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
            }
        }

    override fun getAllOrder(): Flow<Resource<List<Invoice>>> =
        flow {
            emit(Resource.Loading())
            when(val response = remote.getAllOrder().first()) {
                is Response.Success -> {
                    emit(Resource.Success(response.data.toListModel().filter {
                        it.status == COOKING
                    }))
                }
                is Response.Empty -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
            }
        }

    override fun getAllInvoice(): Flow<Resource<List<Invoice>>> =
        flow {
            emit(Resource.Loading())
            when(val response = remote.getAllInvoice().first()) {
                is Response.Success -> {
                    emit(Resource.Success(response.data.toListModel().filter {
                        it.status != COOKING
                    }))
                }
                is Response.Empty -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
            }
        }

    override fun updateInvoiceQueueOrder(invoiceId: String, queueOrder: Int): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading())
            when(val response = remote.updateInvoiceQueueOrder(invoiceId, queueOrder).first()) {
                is Response.Success -> {
                    emit(Resource.Success(null))
                }
                is Response.Empty -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
            }
        }
}