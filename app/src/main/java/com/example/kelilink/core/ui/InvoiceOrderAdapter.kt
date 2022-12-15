package com.example.kelilink.core.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kelilink.core.domain.model.Order
import com.example.kelilink.databinding.ItemOrderBinding
import com.example.kelilink.databinding.ItemOrderWithNoteBinding

class InvoiceOrderAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val ORDER = 0
        const val ORDER_WITH_NOTE  = 1
    }

    private var itemList = ArrayList<Order>()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Order>) {
        itemList.apply {
            clear()
            addAll(items)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when(viewType) {
            ORDER -> {
                val view =
                    ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                OrderViewHolder(view)
            }
            ORDER_WITH_NOTE -> {
                val view =
                    ItemOrderWithNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                OrderWithNoteViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is OrderViewHolder -> {
                holder.bind(itemList[position])
            }
            is OrderWithNoteViewHolder -> {
                holder.bind(itemList[position])
            }
        }
    }

    inner class OrderViewHolder(private val binding: ItemOrderBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(order: Order) {
            with(binding) {
                ioTvAmount.text = "${order.amount}x"
                ioTvName.text = order.name
                ioTvPrice.text = "Rp${order.total_price}"
            }
        }
    }

    inner class OrderWithNoteViewHolder(private val binding: ItemOrderWithNoteBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(order: Order) {
            with(binding) {
                iownTvAmount.text = "${order.amount}x"
                iownTvName.text = order.name
                iownTvPrice.text = "Rp${order.total_price}"
                iownTvNote.text = order.note
            }
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int =
        when(itemList[position].note) {
            "" -> ORDER
            else -> ORDER_WITH_NOTE
        }

}