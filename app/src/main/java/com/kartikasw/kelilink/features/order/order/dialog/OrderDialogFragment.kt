package com.kartikasw.kelilink.features.order.order.dialog

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.VISIBLE
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.kartikasw.kelilink.R
import com.kartikasw.kelilink.core.data.helper.Constants.Extra.EXTRA_INVOICE_ID
import com.kartikasw.kelilink.core.data.helper.Constants.Extra.EXTRA_STORE_ID
import com.kartikasw.kelilink.core.data.helper.Constants.InvoiceStatus.COOKING
import com.kartikasw.kelilink.core.data.helper.Constants.InvoiceStatus.DECLINED
import com.kartikasw.kelilink.core.data.helper.Constants.InvoiceStatus.WAITING
import com.kartikasw.kelilink.core.domain.Resource
import com.kartikasw.kelilink.databinding.FragmentOrderDialogBinding
import com.kartikasw.kelilink.features.transaction.queue.detail.DetailQueueActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDialogFragment: DialogFragment() {

    private val viewModel: OrderDialogViewModel by viewModels()

    private lateinit var invoiceId: String
    private lateinit var storeId: String
    private var isOrderAccepted: Boolean = false

    private var _binding: FragmentOrderDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Kelilink_Dialog_Alert_NoFloating)

        invoiceId = requireArguments().getString(EXTRA_INVOICE_ID)!!
        storeId = requireArguments().getString(EXTRA_STORE_ID)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListener()

        viewModel.listenToOrder(invoiceId, storeId).observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    if(it.data!!.status == COOKING) {
                        setSuccessView()
                        isOrderAccepted = true
                    } else {
                        viewModel.deleteOrder(invoiceId).observe(viewLifecycleOwner) { invoice ->
                            when(invoice) {
                                is Resource.Success -> {
                                    if (it.data.status == DECLINED) {
                                        setDeclinedView()
                                    } else if (it.data.status == WAITING) {
                                        setFailedView()
                                    }
                                    isOrderAccepted = false
                                }
                                is Resource.Error -> {
                                    Log.e(TAG, invoice.message.toString())
                                }
                                else -> {}
                            }
                        }
                    }
                }
                is Resource.Loading -> {

                }
                is Resource.Error -> {
                    Log.e(TAG, it.message.toString())
                }
            }
        }
    }

    private fun setSuccessView() {
        with(binding) {
            odProgress.visibility = GONE
            odIvIcon.apply {
                setImageResource(R.drawable.ic_success)
                visibility = VISIBLE
            }
            odTvTitle.apply {
                text = resources.getString(R.string.title_order_accepted)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                typeface = Typeface.DEFAULT_BOLD
            }
            odTvContent.text = resources.getString(R.string.content_order_accepted)
            odBtnMove.apply {
                text = resources.getString(R.string.btn_to_detail_order)
                visibility = VISIBLE
            }
        }
    }

    private fun setFailedView() {
        with(binding) {
            odProgress.visibility = GONE
            odIvIcon.apply {
                setImageResource(R.drawable.ic_fail)
                visibility = VISIBLE
            }
            odTvTitle.apply {
                text = resources.getString(R.string.title_order_declined_busy)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                typeface = Typeface.DEFAULT_BOLD
            }
            odTvContent.text = resources.getString(R.string.content_order_declined_busy)
            odBtnMove.apply {
                text = resources.getString(R.string.btn_back)
                visibility = VISIBLE
            }
        }
    }

    private fun setDeclinedView() {
        with(binding) {
            odProgress.visibility = GONE
            odIvIcon.apply {
                setImageResource(R.drawable.ic_fail)
                visibility = VISIBLE
            }
            odTvTitle.apply {
                text = resources.getString(R.string.title_order_declined)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                typeface = Typeface.DEFAULT_BOLD
            }
            odTvContent.text = resources.getString(R.string.content_order_declined)
            odBtnMove.apply {
                text = resources.getString(R.string.btn_back)
                visibility = VISIBLE
            }
        }
    }


    private fun setOnClickListener() {
        binding.odBtnMove.setOnClickListener {
            if(isOrderAccepted) {
                val intent = Intent(requireContext(), DetailQueueActivity::class.java)
                intent.apply {
                    putExtra(IS_ORDER_ACCEPTED, true)
                    putExtra(EXTRA_INVOICE_ID, invoiceId)
                }
                startActivity(intent).also {
                    requireActivity().finishAffinity()
                }
            } else {
                dismiss()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
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
        const val TAG = "OrderDialogFragment"
        const val IS_ORDER_ACCEPTED = "is_order_accepted"
    }

}