package com.example.kelilink.features.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.kelilink.R
import com.example.kelilink.core.ui.TransactionSectionAdapter
import com.example.kelilink.databinding.FragmentTransactionBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionFragment : Fragment() {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!

    private lateinit var sectionAdapter: TransactionSectionAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSections()
    }

    private fun setUpSections() {
        with(binding) {
            sectionAdapter = TransactionSectionAdapter(this@TransactionFragment)
            viewPager = tViewpager
            viewPager.adapter = sectionAdapter
            TabLayoutMediator(tTab, viewPager) { tab, position ->
                tab.text = requireContext().resources.getString(TAB_TITLES[position])
            }.attach()
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
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_transaction_queue,
            R.string.tab_transaction_invoice
        )
    }

}