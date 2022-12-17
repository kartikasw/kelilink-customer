package com.kartikasw.kelilink.features.transaction.queue.detail

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.kartikasw.kelilink.core.data.helper.Constants.Extra.EXTRA_INVOICE_ID
import com.kartikasw.kelilink.core.data.helper.Constants.Extra.EXTRA_STORE_ID
import com.kartikasw.kelilink.core.domain.Resource
import com.kartikasw.kelilink.core.domain.model.Invoice
import com.kartikasw.kelilink.core.domain.model.Order
import com.kartikasw.kelilink.core.ui.InvoiceOrderAdapter
import com.kartikasw.kelilink.databinding.ActivityDetailQueueBinding
import com.kartikasw.kelilink.features.MainActivity
import com.kartikasw.kelilink.features.order.order.OrderActivity
import com.kartikasw.kelilink.features.order.order.dialog.OrderDialogFragment.Companion.IS_ORDER_ACCEPTED
import com.kartikasw.kelilink.features.transaction.invoice.detail.DetailInvoiceActivity
import com.kartikasw.kelilink.util.dateFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailQueueActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailQueueBinding

    private val queueViewModel: DetailQueueViewModel by viewModels()

    private lateinit var invoiceId: String

    private var storeName: String = ""

    private lateinit var storeId: String

    private var isOrderAccepted: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailQueueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        invoiceId = intent.getStringExtra(EXTRA_INVOICE_ID)!!
        isOrderAccepted = intent.getBooleanExtra(IS_ORDER_ACCEPTED, false)

        setUpToolbar()

        showInvoice()

        if(!isOrderAccepted) {
            storeId = intent.getStringExtra(EXTRA_STORE_ID)!!
            setQueueData()
        }
    }

    private fun setQueueData() {
        queueViewModel.getStoreById(storeId).observe(this) {
            when(it) {
                is Resource.Success -> {
                    val data = it.data!!
                    val queueOrder = data.queue.indexOf(invoiceId) + 1
                    queueViewModel.updateQueueOrder(invoiceId, queueOrder).observe(this) { update ->
                        when (update) {
                            is Resource.Success -> {
                                binding.dqTvQueueOrder.text = queueOrder.toString()
                            }
                            is Resource.Error -> {
                                Log.d(DetailInvoiceActivity.TAG, it.message.toString())
                            }
                            else -> {}
                        }
                    }
                }
                is Resource.Error -> {
                    Log.d(DetailInvoiceActivity.TAG, it.message.toString())
                }
                else -> {}
            }
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.dqToolbar)
        supportActionBar?.apply {
            title = storeName
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun showInvoice() {
        queueViewModel.getInvoiceById(invoiceId).observe(this) {
            when(it) {
                is Resource.Success -> {
                    storeId = it.data!!.store_id
                    supportActionBar?.title = it.data.store_name
                    setUpInvoiceView(it.data)
                    showOrder()
                }
                is Resource.Loading -> {
                    showInvoiceDetailView(false)
                    showLoadingState(true)
                }
                is Resource.Error -> {
                    showLoadingState(false)
                    Log.e(TAG, it.message.toString())
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpInvoiceView(data: Invoice) {
        with(binding) {
            dqContent.cdoLayoutLocation.colTvAddress.text = data.address
            dqContent.cdoLayoutLocation.colTvDistance.visibility = ViewGroup.INVISIBLE
            dqTvTotalPrice.text = "Rp${data.total_price}"
            dqTvQueueOrder.text = data.queue_order.toString()
            dqTvTime.text = dateFormat.format(data.time)
            dqTvId.text = data.id

            dqContent.cdoLayoutLocation.root.setOnClickListener {
                val gmmIntentUri = Uri.parse("geo:${data.store_lat},${data.store_lon}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage(OrderActivity.MAPS_PACKAGE)
                try {
                    startActivity(mapIntent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this@DetailQueueActivity, "Aplikasi tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showOrder() {
        queueViewModel.getInvoiceOrder(invoiceId).observe(this) {
            when(it) {
                is Resource.Success -> {
                    showLoadingState(false)
                    setUpOrderView(it.data!!)
                    showInvoiceDetailView(true)

                }
                is Resource.Error -> {
                    showLoadingState(false)
                    Log.e(TAG, it.message.toString())
                }
                else -> {}
            }
        }
    }

    private fun setUpOrderView(data: List<Order>) {
        val orderAdapter = InvoiceOrderAdapter()

        orderAdapter.setItems(data)

        with(binding.dqContent.cdoRvOrder) {
            layoutManager = LinearLayoutManager(this@DetailQueueActivity)
            adapter = orderAdapter
        }
    }

    private fun showLoadingState(state: Boolean) {
        binding.dqContent.cdoLoading.root.isVisible = state
    }

    private fun showInvoiceDetailView(state: Boolean) {
        binding.dqContent.cdoLayoutContent.isVisible = state
    }

    companion object {
        const val TAG = "DetailQueueActivity"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if(isOrderAccepted) {
            val intent = Intent(this, MainActivity::class.java)
//            intent.apply {
//                flags = FLAG_ACTIVITY_CLEAR_TOP
//            }
            startActivity(intent).also { finishAffinity() }
        } else {
            super.onBackPressed()
        }
    }
}