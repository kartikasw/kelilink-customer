package com.kartikasw.kelilink.features.order.store

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
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.kartikasw.kelilink.R
import com.kartikasw.kelilink.core.data.helper.Constants.Extra.EXTRA_MENU_ID
import com.kartikasw.kelilink.core.data.helper.Constants.Extra.EXTRA_MENU_NOTE
import com.kartikasw.kelilink.core.data.helper.Constants.Extra.EXTRA_STORE_ID
import com.kartikasw.kelilink.core.data.helper.Constants.Extra.EXTRA_STORE_NAME
import com.kartikasw.kelilink.core.data.helper.Constants.Extra.EXTRA_TOTAL_PRICE
import com.kartikasw.kelilink.core.domain.Resource
import com.kartikasw.kelilink.core.domain.model.Menu
import com.kartikasw.kelilink.core.domain.model.Store
import com.kartikasw.kelilink.core.ui.MenuAdapter
import com.kartikasw.kelilink.databinding.ActivityStoreBinding
import com.kartikasw.kelilink.features.order.order.OrderActivity
import com.kartikasw.kelilink.features.order.store.detail_menu.MenuDialogFragment
import com.kartikasw.kelilink.features.order.store.note.NoteFragment
import com.kartikasw.kelilink.util.distanceInKm
import com.kartikasw.kelilink.util.withCurrencyFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoreBinding

    private val viewModel: StoreViewModel by viewModels()

    private lateinit var storeId: String
    private lateinit var storeName: String
    private var userLatitude: Double = 0.0
    private var userLongitude: Double = 0.0
    private var storeLatitude: Double = 0.0
    private var storeLongitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.deleteAllMenu()

        binding = ActivityStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setData()

        setUpToolbar()

        setUpButtonView()

        showDetailStore()

        showTotalItemAndPrice()

        setOnClickListener()

        setOnSwipeRefreshListener()
    }

    private fun setData() {
        storeId = intent?.getStringExtra(EXTRA_STORE_ID).toString()
        binding.sEmpty.seTvTitle.text = resources.getString(R.string.title_cant_order_menu)

        lifecycleScope.launch {
            viewModel.getMyProfile().collect {
                when(it) {
                    is Resource.Success -> {
                        userLatitude = it.data!!.lat
                        userLongitude = it.data.lon
                    }
                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.sToolbar)
        supportActionBar?.apply {
            title = ""
        }
    }

    private fun setUpButtonView() {
        viewModel.totalPrice.observe(this) {
            with(binding.sBtnOrder) {
                if(it < 7000) {
                    setBackgroundResource(
                        R.drawable.bg_button_primary_disabled
                    )
                } else {
                    setBackgroundResource(
                        R.drawable.bg_button_primary
                    )
                }
            }
        }
    }

    private fun showDetailStore() {
        viewModel.getStoreById(storeId).observe(this) {
            when(it) {
                is Resource.Success -> {
                    storeName = it.data!!.name
                    storeLatitude = it.data.lat
                    storeLongitude = it.data.lon

                    setUpDetailStoreView(
                        it.data.copy(
                            distance = distanceInKm(
                                userLatitude,
                                userLongitude,
                                it.data.lat,
                                it.data.lon
                            )
                        )
                    )

                    if(it.data.operating_status) {
                        showEmpty(false)
                    } else {
                        showEmpty(true)
                    }

                    showMenu()
                }
                is Resource.Error -> {
                    showLoadingState(false)
                    Log.e(TAG, it.message.toString())
                }
                else -> {
                    showStoreInfo(false)
                    showLoadingState(true)
                }
            }
        }
    }

    private fun setUpDetailStoreView(data: Store) {
        with(binding) {
            Glide.with(sIvStore.context)
                .load(data.image)
                .into(sIvStore)

            var category = ""
            for(text in data.categories) {
                category = if(data.categories[data.categories.lastIndex] == text) {
                    "$category$text"
                } else {
                    "$category$text, "
                }
            }

            sLayoutInfo.apply {
                csiTvStoreName.text = data.name
                csiTvCategory.text = category
                csiTvQueue.text = resources.getString(R.string.total_queue_format, data.queue.size)
                csiTvDistance.text = resources.getString(R.string.distance_format, String.format("% .1f", data.distance))
            }
        }
    }

    private fun showMenu() {
        viewModel.getMenuByStoreId(storeId).observe(this@StoreActivity) {
            when(it) {
                is Resource.Success -> {
                    if(it.data != null) {
                        setUpMenuView(it.data)
                        showLoadingState(false)
                        showStoreInfo(true)
                    }
                }
                is Resource.Error -> {
                    showLoadingState(false)
                    Log.e(TAG, it.message.toString())
                }
                else -> {}
            }
        }
    }

    private fun setUpMenuView(data: List<Menu>?) {
        val menuAdapter = MenuAdapter()

        menuAdapter.apply {
            onItemClick = {
                val  dialog = MenuDialogFragment()
                val bundle = Bundle()
                bundle.putString(EXTRA_MENU_ID, it.id)
                dialog.apply {
                    arguments = bundle
                    show(supportFragmentManager, MenuDialogFragment.TAG)
                }
            }

            onSelectClick = {
                increaseAmount(it)
                viewModel.apply {
                    updatePrice(it.price)
                    updateItem(1)
                }
            }

            onIncreaseClick = {
                increaseAmount(it)
                viewModel.apply {
                    updatePrice(it.price)
                    updateItem(1)
                }
            }

            onDecreaseClick = {
                decreaseAmount(it)
                viewModel.apply {
                    updatePrice(-it.price)
                    updateItem(-1)
                }
            }

            onNoteClick = {
                val toAddNote = NoteFragment()
                val bundle = Bundle()
                bundle.apply {
                    putString(EXTRA_MENU_ID, it.id)
                    putString(EXTRA_MENU_NOTE, it.note)
                }
                toAddNote.arguments = bundle
                toAddNote.show(supportFragmentManager, TAG)
            }

            onEditNoteClick = {
                val toAddNote = NoteFragment()
                val bundle = Bundle()
                bundle.apply {
                    putString(EXTRA_MENU_ID, it.id)
                    putString(EXTRA_MENU_NOTE, it.note)
                }
                toAddNote.arguments = bundle
                toAddNote.show(supportFragmentManager, TAG)
            }
        }

        menuAdapter.setItems(data!!)

        with(binding.sRvMenu) {
            layoutManager = GridLayoutManager(this@StoreActivity, 2)
            adapter = menuAdapter
        }
    }

    private fun increaseAmount(menu: Menu) {
        with(menu) {
            viewModel.updateAmountAndTotalPriceMenu(id, amount + 1, total_price + price)
        }
    }

    private fun decreaseAmount(menu: Menu) {
        with(menu) {
            viewModel.updateAmountAndTotalPriceMenu(id, amount - 1, total_price - price)
        }
    }

    private fun showLoadingState(status: Boolean) {
        binding.sLoading.root.isVisible = status
    }

    private fun showStoreInfo(state: Boolean) {
        binding.sLayoutInfo.root.isVisible = state
    }

    private fun showEmpty(status: Boolean) {
        binding.sEmpty.root.isVisible = status
        binding.sRvMenu.isVisible = !status
    }

    private fun showTotalItemAndPrice() {
        with(binding) {
            viewModel.apply {
                totalItem.observe(this@StoreActivity) {
                    sTvItem.text = resources.getString(R.string.basket_format, it)
                }

                totalPrice.observe(this@StoreActivity) {
                    dpTvPrice.text = it.withCurrencyFormat()
                }
            }
        }
    }

    private fun setOnClickListener() {
        binding.sBtnOrder.setOnClickListener {
            val totalPrice = viewModel.totalPrice.value
            if (totalPrice != null) {
                if(totalPrice >= 7000) {
                    val intent = Intent(this, OrderActivity::class.java)
                    intent.apply {
                        putExtra(EXTRA_STORE_ID, storeId)
                        putExtra(EXTRA_TOTAL_PRICE, totalPrice)
                        putExtra(EXTRA_STORE_NAME, storeName)
                    }
                    startActivity(intent)
                }
            }
        }

        binding.sLayoutInfo.csiLayoutLocation.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:$storeLatitude,$storeLongitude")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            try {
                startActivity(mapIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "Aplikasi tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }

        binding.sIbBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setOnSwipeRefreshListener() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getStoreById(storeId).observe(this) {
                when(it) {
                    is Resource.Success -> {
                        binding.swipeRefresh.isEnabled = true
                        binding.swipeRefresh.isRefreshing = false
                        binding.sLayoutInfo.csiTvQueue.text = resources.getString(R.string.total_queue_format, it.data!!.queue.size)

                        if(it.data.operating_status) {
                            showEmpty(false)
                        } else {
                            showEmpty(true)
                        }
                    }
                    is Resource.Loading -> {
                        binding.swipeRefresh.isEnabled = false
                    }
                    is Resource.Error -> {
                        binding.swipeRefresh.isEnabled = true
                        binding.swipeRefresh.isRefreshing = false
                        Log.e(TAG, it.message.toString())
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.apply {
            deleteAllMenu()
            updateItem(0)
            updatePrice(0)
        }
    }

    companion object {
        const val TAG = "StoreActivity"
    }
}