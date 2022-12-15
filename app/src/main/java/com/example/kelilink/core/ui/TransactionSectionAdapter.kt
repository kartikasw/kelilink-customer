package com.example.kelilink.core.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.kelilink.features.transaction.invoice.InvoiceFragment
import com.example.kelilink.features.transaction.queue.QueueFragment

class TransactionSectionAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = QueueFragment()
            1 -> fragment = InvoiceFragment()
        }
        return fragment as Fragment
    }
}