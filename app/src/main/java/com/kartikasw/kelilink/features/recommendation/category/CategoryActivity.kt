package com.kartikasw.kelilink.features.recommendation.category

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kartikasw.kelilink.core.data.helper.Constants.Extra.EXTRA_STORE_CATEGORY
import com.kartikasw.kelilink.core.data.helper.Constants.Extra.EXTRA_STORE_ID
import com.kartikasw.kelilink.core.domain.Resource
import com.kartikasw.kelilink.core.domain.model.Store
import com.kartikasw.kelilink.core.ui.StoreAdapter
import com.kartikasw.kelilink.databinding.ContentRecyclerViewWithAppbarBinding
import com.kartikasw.kelilink.features.order.store.StoreActivity
import com.kartikasw.kelilink.util.distanceInKm
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryActivity : AppCompatActivity() {
    private lateinit var binding: ContentRecyclerViewWithAppbarBinding

    private val viewModel: CategoryViewModel by viewModels()

    private lateinit var category: String

    private var userLatitude: Double = 0.0
    private var userLongitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ContentRecyclerViewWithAppbarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        category = intent?.getStringExtra(EXTRA_STORE_CATEGORY).toString()

        setUpToolbar()

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
                    is Resource.Loading -> {
                        showLoadingState(true)
                    }
                }
            }
        }

        showRecommendation()

        setOnSwipeRefreshListener()
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.crvwaToolbar)
        supportActionBar?.apply {
            title = category
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun showRecommendation() {
        viewModel.getStoreByCategory(category).observe(this) {
            when(it) {
                is Resource.Success -> {
                    if(!it.data.isNullOrEmpty()) {
                        setUpStoreView(it.data)
                    } else {
                        showEmptyState(true)
                    }
                    showLoadingState(false)
                }
                is Resource.Error -> {
                    showEmptyState(false)
                    showLoadingState(false)
                    Log.e(TAG, it.message.toString())
                }
                is Resource.Loading -> {
                    showLoadingState(true)
                }
            }
        }
    }

    private fun setUpStoreView(data: List<Store>?) {
        val storeAdapter = StoreAdapter()

        storeAdapter.onItemClick = {
            if(it.operating_status) {
                val intent = Intent(this, StoreActivity::class.java)
                    .putExtra(EXTRA_STORE_ID, it.id)
                startActivity(intent)
            }
        }

        storeAdapter.setItems(
            data?.map {
                it.copy(
                    distance = distanceInKm(
                        userLatitude,
                        userLongitude,
                        it.lat,
                        it.lon
                    )
                )
            }
        )

        with(binding.crvwaRv) {
            layoutManager = LinearLayoutManager(this@CategoryActivity)
            adapter = storeAdapter
        }
    }

    private fun showLoadingState(status: Boolean) {
        binding.crvwaLoading.root.isVisible = status
    }

    private fun showEmptyState(status: Boolean) {
        binding.crwaEmpty.root.isVisible = status
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setOnSwipeRefreshListener() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshAllStore(category).observe(this, ::refreshResponse)
        }
    }

    private fun refreshResponse(resource: Resource<List<Store>>) {
        when(resource) {
            is Resource.Success -> {
                if(resource.data == null) {
                    showEmptyState(true)
                } else {
                    showEmptyState(false)
                }
                setUpStoreView(resource.data)
                binding.swipeRefresh.isEnabled = true
                binding.swipeRefresh.isRefreshing = false
            }
            is Resource.Loading -> {
                binding.swipeRefresh.isEnabled = false
            }

            is Resource.Error -> {
                binding.swipeRefresh.isEnabled = true
                binding.swipeRefresh.isRefreshing = false
                Snackbar.make(binding.root, resource.message.toString(), Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    companion object {
        const val TAG = "CategoryActivity"
    }
}