package com.kartikasw.kelilink.features.transaction.invoice.detail

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
import com.kartikasw.kelilink.core.data.helper.Constants.Extra.EXTRA_STORE_NAME
import com.kartikasw.kelilink.core.domain.Resource
import com.kartikasw.kelilink.core.domain.model.Invoice
import com.kartikasw.kelilink.core.domain.model.Order
import com.kartikasw.kelilink.core.ui.InvoiceOrderAdapter
import com.kartikasw.kelilink.databinding.ActivityDetailInvoiceBinding
import com.kartikasw.kelilink.features.order.order.OrderActivity
import com.kartikasw.kelilink.features.transaction.queue.detail.DetailQueueActivity
import com.kartikasw.kelilink.util.dateFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailInvoiceActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityDetailInvoiceBinding

    private val viewModel: DetailInvoiceViewModel by viewModels()

    private lateinit var invoiceId: String

    private lateinit var storeId: String

    private lateinit var storeName: String

    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        invoiceId = intent.getStringExtra(EXTRA_INVOICE_ID)!!
        storeName = intent.getStringExtra(EXTRA_STORE_NAME)!!

        setUpToolbar()

        showInvoice()
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.diToolbar)
        supportActionBar?.apply {
            title = storeName
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun showInvoice() {
        viewModel.getInvoiceById(invoiceId).observe(this) {
            when(it) {
                is Resource.Success -> {
                    storeId = it.data!!.store_id
                    userId = it.data.user_id
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
                    Log.e(DetailQueueActivity.TAG, it.message.toString())
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpInvoiceView(data: Invoice) {
        with(binding) {
            diContent.cdoLayoutLocation.colTvAddress.text = data.address
            diContent.cdoLayoutLocation.colTvDistance.visibility = ViewGroup.INVISIBLE
            diTvTotal.text = "Rp${data.total_price}"
            diTvTime.text = dateFormat.format(data.time)
            diTvId.text = data.id

            diContent.cdoLayoutLocation.root.setOnClickListener {
                val gmmIntentUri = Uri.parse("geo:${data.store_lat},${data.store_lon}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage(OrderActivity.MAPS_PACKAGE)
                try {
                    startActivity(mapIntent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this@DetailInvoiceActivity, "Aplikasi tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showOrder() {
        viewModel.getInvoiceOrder(invoiceId).observe(this) {
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

        with(binding.diContent.cdoRvOrder) {
            layoutManager = LinearLayoutManager(this@DetailInvoiceActivity)
            adapter = orderAdapter
        }
    }

    private fun showLoadingState(state: Boolean) {
        binding.diContent.cdoLoading.root.isVisible = state
    }

    private fun showInvoiceDetailView(state: Boolean) {
        binding.diContent.cdoLayoutContent.isVisible = state
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val TAG = "DetailInvoiceActivity"
    }
}