package com.example.kelilink.features.recommendation.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kelilink.R
import com.example.kelilink.core.data.helper.Constants.Extra.EXTRA_STORE_CATEGORY
import com.example.kelilink.core.data.helper.Constants.Extra.EXTRA_STORE_ID
import com.example.kelilink.core.data.helper.Constants.Extra.EXTRA_USER_LATITUDE
import com.example.kelilink.core.data.helper.Constants.Extra.EXTRA_USER_LONGITUDE
import com.example.kelilink.core.data.source.remote.service.location.LocationPermissionState
import com.example.kelilink.core.domain.Resource
import com.example.kelilink.core.domain.model.Category
import com.example.kelilink.core.domain.model.Store
import com.example.kelilink.core.ui.CategoryAdapter
import com.example.kelilink.core.ui.StoreAdapter
import com.example.kelilink.databinding.FragmentHomeBinding
import com.example.kelilink.features.order.store.StoreActivity
import com.example.kelilink.features.profile.ProfileActivity
import com.example.kelilink.features.recommendation.category.CategoryActivity
import com.example.kelilink.util.distanceInKm
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private var userLatitude: Double = 0.0
    private var userLongitude: Double = 0.0

    private lateinit var storeAdapter: StoreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val locationPermissionState = LocationPermissionState(requireActivity()) {
            it.accessFineLocationGranted.observe(viewLifecycleOwner) { boolean ->
                if(boolean) {
                    viewModel.getCurrentAddress()
                }
            }
        }

        locationPermissionState.requestPermissions()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storeAdapter = StoreAdapter()
        with(binding.hLayoutRvRecommendation.crvRv) {
            layoutManager = LinearLayoutManager(activity)
            adapter = storeAdapter
        }

        showCategory()

        setUpAddressView()

        setOnClickListener()

        setOnSwipeRefreshListener()

    }

    private fun setUpAddressView() {
        with(viewModel) {
            showProgress.observe(viewLifecycleOwner) { progress ->
                if(!progress) {
                    location.observe(viewLifecycleOwner) { location ->
                        if(location.first.isNotEmpty()) {
                            userLatitude = location.second.latitude
                            userLongitude = location.second.longitude
                            binding.hTvAddress.text = location.first
                            showRecommendation()
                        } else {
                            binding.hTvAddress.text =
                                activity?.resources?.getString(R.string.placeholder_location)
                        }
                    }
                } else {
                    binding.hTvAddress.text =
                        activity?.resources?.getString(R.string.placeholder_location)
                }
            }
        }
    }

    private fun showCategory() {
        val categoryAdapter = CategoryAdapter()

        categoryAdapter.setItems(listCategories)

        categoryAdapter.onItemClick = { category ->
            if(userLatitude != 0.0) {
                val intent = Intent(activity, CategoryActivity::class.java)
                    .putExtra(EXTRA_STORE_CATEGORY, category.name)
                    .putExtra(EXTRA_USER_LATITUDE, userLatitude)
                    .putExtra(EXTRA_USER_LONGITUDE, userLongitude)
                startActivity(intent)
            } else {
                Snackbar.make(
                    binding.root,
                    resources.getString(R.string.error_location),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        with(binding.hLayoutRvCategory.crvRv) {
            layoutManager = GridLayoutManager(activity, 4)
            adapter = categoryAdapter
        }
    }

    private val listCategories: ArrayList<Category>
        get() {
            val name = requireContext().resources.getStringArray(R.array.category_name)
            val icon = requireContext().resources.obtainTypedArray(R.array.category_icon)
            val listCategory = ArrayList<Category>()
            for (i in name.indices) {
                val category = Category(name[i], icon.getResourceId(i, -1))
                listCategory.add(category)
            }
            icon.recycle()
            return listCategory
        }

    private fun showRecommendation() {
        viewModel.getAllStore().observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    showLoadingState(false)
                    if(!it.data.isNullOrEmpty()) {
                        setUpStoreView(it.data)
                    } else {
                        showEmptyState(true)
                    }
                }
                is Resource.Loading -> {
                    showEmptyState(false)
                    showLoadingState(true)
                }
                is Resource.Error -> {
                    showEmptyState(false)
                    showLoadingState(false)
                    Log.e(TAG, it.message.toString())
                    Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun setUpStoreView(data: List<Store>?) {
        storeAdapter.onItemClick = {
            val intent = Intent(activity, StoreActivity::class.java)
                .putExtra(EXTRA_STORE_ID, it.id)
            startActivity(intent)
        }

        storeAdapter.setItems(
            data?.map {
                it.copy(distance = distanceInKm(userLatitude, userLongitude, it.lat, it.lon))
            }
        )
    }

    private fun showLoadingState(status: Boolean) {
        binding.hLoading.root.isVisible = status
    }

    private fun showEmptyState(status: Boolean) {
        binding.hEmpty.root.isVisible = status
    }

    private fun setOnClickListener() {
        with(binding) {
            hIbProfile.setOnClickListener {
                startActivity(Intent(activity, ProfileActivity::class.java))
            }
        }
    }

    private fun setOnSwipeRefreshListener() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshAllStore().observe(viewLifecycleOwner, ::refreshResponse)
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
                showLoadingState(false)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "HomeFragment"
    }

}