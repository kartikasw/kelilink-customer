package com.example.kelilink.features.order.order

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kelilink.core.data.helper.Constants.Extra.EXTRA_INVOICE_ID
import com.example.kelilink.core.data.helper.Constants.Extra.EXTRA_STORE_ID
import com.example.kelilink.core.data.helper.Constants.Extra.EXTRA_STORE_NAME
import com.example.kelilink.core.data.helper.Constants.Extra.EXTRA_TOTAL_PRICE
import com.example.kelilink.core.data.helper.Constants.InvoiceStatus.WAITING
import com.example.kelilink.core.domain.Resource
import com.example.kelilink.core.domain.model.*
import com.example.kelilink.core.ui.OrderAdapter
import com.example.kelilink.databinding.ActivityOrderBinding
import com.example.kelilink.features.order.order.dialog.OrderDialogFragment
import com.example.kelilink.features.order.order.dialog.OrderLoadingDialog
import com.example.kelilink.util.distanceInKm
import com.example.kelilink.util.withCurrencyFormat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class OrderActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityOrderBinding

    private val viewModel: OrderViewModel by viewModels()

    private lateinit var loading: OrderLoadingDialog

    private lateinit var storeAddress: String
    private lateinit var storeId: String
    private lateinit var storeImage: String
    private var storeLatitude: Double = 0.0
    private var storeLongitude: Double = 0.0
    private lateinit var storeName: String
    private lateinit var storeToken: String

    private var totalPrice: Int? = 0

    private var userLatitude: Double = 0.0
    private var userLongitude: Double = 0.0
    private lateinit var userPhoneNumber: String

    private var orderList = ArrayList<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding  .inflate(layoutInflater)
        setContentView(binding.root)

        loading = OrderLoadingDialog(this)

        setData()

        setUpToolbar()

        setUpBottomBar()

        setOnClickListener()
    }

    private fun setData() {
        storeId = intent?.getStringExtra(EXTRA_STORE_ID).toString()
        storeName = intent?.getStringExtra(EXTRA_STORE_NAME).toString()
        totalPrice = intent?.getIntExtra(EXTRA_TOTAL_PRICE, 0)

        lifecycleScope.launch {
            viewModel.getMyProfile().collect {
                when(it) {
                    is Resource.Success -> {
                        userLatitude = it.data!!.lat
                        userLongitude = it.data.lon
                        userPhoneNumber = it.data.phone_number
                    }
                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                    }
                    else -> {}
                }
            }
        }

        viewModel.getStoreById(storeId).observe(this) {
            when(it) {
                is Resource.Success -> {
                    storeAddress = it.data!!.address
                    storeImage = it.data.image
                    storeLatitude = it.data.lat
                    storeLongitude = it.data.lon
                    storeToken = it.data.fcm_token
                    showPickUpLocation(it.data)
                }
                is Resource.Loading -> {
                    showLoadingState(true)
                }
                is Resource.Error -> {
                    showLoadingState(false)
                    Log.e(TAG, it.message.toString())
                }
            }
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.oToolbar)
        supportActionBar?.apply {
            title = storeName
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpBottomBar() {
        binding.oTvTotal.text = totalPrice?.withCurrencyFormat()
    }

    private fun showPickUpLocation(data: Store) {
        setUpPickUpLocationView(data)
        showOrderedMenu()
    }

    @SuppressLint("SetTextI18n")
    private fun setUpPickUpLocationView(store: Store) {
        val distance = distanceInKm(userLatitude, userLongitude, store.lat, store.lon)

        with(binding.oContent.cdoLayoutLocation) {
            colTvAddress.text = store.address
            colTvDistance.text = "${String.format("% .1f", distance)} km"
        }
    }

    private fun showOrderedMenu() {
        viewModel.getOrderedMenu().observe(this) {
            when(it) {
                is Resource.Success -> {
                    setOrder(it.data)
                    setUpOrderView(it.data)
                    showLoadingState(false)
                    showContent()
                }
                is Resource.Error -> {
                    showLoadingState(false)
                    Log.e(TAG, it.message.toString())
                }
                else -> {}
            }
        }
    }

    private fun setUpOrderView(data: List<Menu>?) {
        val orderAdapter = OrderAdapter()

        orderAdapter.setItems(data!!)

        with(binding.oContent.cdoRvOrder) {
            layoutManager = LinearLayoutManager(this@OrderActivity)
            adapter = orderAdapter
        }
    }

    private fun setOnClickListener() {
        with(binding) {
            oContent.cdoLayoutLocation.root.setOnClickListener {
                val gmmIntentUri = Uri.parse("geo:$storeLatitude,$storeLongitude")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage(MAPS_PACKAGE)
                try {
                    startActivity(mapIntent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this@OrderActivity, "Aplikasi tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }

            oBtnOrder.setOnClickListener {
                val userId = Firebase.auth.currentUser?.uid!!
                val timeExpire = Calendar.getInstance()
                val time = Calendar.getInstance()
                timeExpire.add(Calendar.SECOND, 30)
                val fcmToken = viewModel.getFcmToken()

                val invoice = Invoice(
                    address = storeAddress,
                    id = "",
                    queue_order = 0,
                    status = WAITING,
                    store_id = storeId,
                    store_image = storeImage,
                    store_lat = storeLatitude,
                    store_lon = storeLongitude,
                    store_name = storeName,
                    time = time.time,
                    time_expire = timeExpire.time,
                    total_price = totalPrice!!,
                    user_fcm_token = fcmToken,
                    user_id = userId,
                    user_phone_number = userPhoneNumber
                )

                viewModel.makeOrder(invoice, orderList).observe(this@OrderActivity) { invoiceData ->
                    when(invoiceData) {
                        is Resource.Success -> {
                            viewModel.sendFcm(
                                Fcm(
                                    storeToken,
                                    FcmData()
                                )
                            ).observe(this@OrderActivity) {
                                when(it) {
                                    is Resource.Success -> {
                                        loading.dismiss()
                                        oBtnOrder.isEnabled = true
                                        val  dialog = OrderDialogFragment()
                                        val bundle = Bundle()
                                        bundle.apply {
                                            putString(EXTRA_INVOICE_ID, invoiceData.data!!.id)
                                            putString(EXTRA_STORE_ID, storeId)
                                        }
                                        dialog.apply {
                                            arguments = bundle
                                            show(supportFragmentManager, OrderDialogFragment.TAG)
                                            isCancelable = false
                                        }
                                    }
                                    is Resource.Error -> { Log.e(TAG, it.message.toString()) }
                                    else -> {}
                                }
                            }
                        }
                        is Resource.Loading -> {
                            loading.show()
                            oBtnOrder.isEnabled = false
                        }
                        is Resource.Error -> {
                            loading.dismiss()
                            Toast.makeText(this@OrderActivity, invoiceData.message, Toast.LENGTH_SHORT).show()
                            Log.e(TAG, invoiceData.message.toString())
                        }
                    }
                }
            }
        }
    }

    private fun setOrder(orders: List<Menu>?) {
        orderList.apply {
            clear()
            addAll(orders!!.map { it.toOrderModel() })
        }
    }

    private fun Menu.toOrderModel(): Order =
        Order(
            amount, name, note, price, total_price, unit
        )

    private fun showLoadingState(state: Boolean) {
        binding.oContent.cdoLoading.root.isVisible = state
    }

    private fun showContent() {
        binding.oContent.cdoLayoutContent.isVisible = true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val TAG = "OrderActivity"
        const val MAPS_PACKAGE = "com.google.android.apps.maps"
    }
}