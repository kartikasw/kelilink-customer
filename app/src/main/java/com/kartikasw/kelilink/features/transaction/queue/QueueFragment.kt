package com.kartikasw.kelilink.features.transaction.queue

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kartikasw.kelilink.R
import com.kartikasw.kelilink.core.data.helper.Constants.Extra.EXTRA_INVOICE_ID
import com.kartikasw.kelilink.core.data.helper.Constants.Extra.EXTRA_STORE_ID
import com.kartikasw.kelilink.core.domain.Resource
import com.kartikasw.kelilink.core.domain.model.Invoice
import com.kartikasw.kelilink.core.ui.InvoiceAdapter
import com.kartikasw.kelilink.databinding.ContentRecyclerViewInvoiceBinding
import com.kartikasw.kelilink.features.order.order.dialog.OrderDialogFragment.Companion.IS_ORDER_ACCEPTED
import com.kartikasw.kelilink.features.transaction.queue.detail.DetailQueueActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QueueFragment : Fragment() {

    private var _binding: ContentRecyclerViewInvoiceBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QueueViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContentRecyclerViewInvoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showOrder()

        setOnSwipeRefreshListener()
    }

    private fun showOrder() {
        viewModel.getAllOrder().observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    showLoadingState(false)
                    if(!it.data.isNullOrEmpty()) {
                        setUpOrderView(it.data)
                    } else {
                        showEmptyState(true)
                    }
                }
                is Resource.Loading -> {
                    showEmptyState(false)
                    showLoadingState(true)
                }
                is Resource.Error -> {
                    showLoadingState(false)
                    Log.e(TAG, it.message.toString())
                }
            }
        }
    }

    private fun setUpOrderView(data: List<Invoice>?) {
        val orderAdapter = InvoiceAdapter()

        orderAdapter.onItemClick = {
            val intent = Intent(requireContext(), DetailQueueActivity::class.java)
                .putExtra(EXTRA_INVOICE_ID, it.id)
                .putExtra(IS_ORDER_ACCEPTED, false)
                .putExtra(EXTRA_STORE_ID, it.store_id)
            startActivity(intent)
        }

        orderAdapter.setItems(data)

        with(binding.crviRv) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }
    }

    private fun showLoadingState(state: Boolean) {
        binding.crviLoading.root.isVisible = state
    }

    private fun showEmptyState(state: Boolean) {
        with(binding.crviEmpty) {
            seTvTitle.text = requireContext().resources.getString(R.string.title_order_empty)
            seTvContent.text = requireContext().resources.getString(R.string.content_order_empty)
            root.isVisible = state
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

    private fun setOnSwipeRefreshListener() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshAllOrder().observe(viewLifecycleOwner, ::refreshResponse)
        }
    }

    private fun refreshResponse(resource: Resource<List<Invoice>>) {
        when(resource) {
            is Resource.Success -> {
                if(resource.data.isNullOrEmpty()) {
                    showEmptyState(true)
                } else {
                    showEmptyState(false)
                }
                setUpOrderView(resource.data)
                binding.swipeRefresh.isEnabled = true
                binding.swipeRefresh.isRefreshing = false
            }
            is Resource.Loading -> {
                binding.swipeRefresh.isEnabled = false
            }

            is Resource.Error -> {
                binding.swipeRefresh.isEnabled = true
                binding.swipeRefresh.isRefreshing = false
                Log.e(TAG, resource.message.toString())
            }
        }
    }

    companion object {
        const val TAG = "QueueFragment"
    }

}