package com.example.kelilink.features.transaction.invoice

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
import com.example.kelilink.core.data.helper.Constants.Extra.EXTRA_INVOICE_ID
import com.example.kelilink.core.data.helper.Constants.Extra.EXTRA_STORE_NAME
import com.example.kelilink.core.domain.Resource
import com.example.kelilink.core.domain.model.Invoice
import com.example.kelilink.core.ui.InvoiceAdapter
import com.example.kelilink.databinding.ContentRecyclerViewInvoiceBinding
import com.example.kelilink.features.transaction.invoice.detail.DetailInvoiceActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvoiceFragment : Fragment() {

    private var _binding: ContentRecyclerViewInvoiceBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InvoiceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContentRecyclerViewInvoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showInvoice()

        setOnSwipeRefreshListener()
    }

    private fun showInvoice() {
        viewModel.getAllInvoice().observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    showLoadingState(false)
                    if(!it.data.isNullOrEmpty()) {
                        setUpInvoiceView(it.data)
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

    private fun setUpInvoiceView(data: List<Invoice>?) {
        val invoiceAdapter = InvoiceAdapter()

        invoiceAdapter.onItemClick = {
            val intent = Intent(requireContext(), DetailInvoiceActivity::class.java)
                .putExtra(EXTRA_INVOICE_ID, it.id)
                .putExtra(EXTRA_STORE_NAME, it.store_name)
            startActivity(intent)
        }

        invoiceAdapter.setItems(data)

        with(binding.crviRv) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = invoiceAdapter
        }
    }

    private fun showLoadingState(state: Boolean) {
        binding.crviLoading.root.isVisible = state
    }

    private fun showEmptyState(state: Boolean) {
        with(binding.crviEmpty) {
            seTvTitle.text = requireContext().resources.getString(com.example.kelilink.R.string.title_order_empty)
            seTvContent.text = requireContext().resources.getString(com.example.kelilink.R.string.content_order_empty)
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
            viewModel.refreshAllInvoice().observe(viewLifecycleOwner, ::refreshResponse)
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
                setUpInvoiceView(resource.data)
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
        const val TAG = "InvoiceFragment"
    }
}