package com.example.kelilink.core.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kelilink.R
import com.example.kelilink.core.domain.model.Menu
import com.example.kelilink.databinding.ItemOrderBinding
import com.example.kelilink.databinding.ItemOrderWithNoteBinding

class OrderAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val ORDER = 0
        const val ORDER_WITH_NOTE  = 1
    }

    private var itemList = ArrayList<Menu>()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Menu>) {
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
        fun bind(menu: Menu) {
            with(binding) {
                ioTvAmount.text = itemView.context.resources.getString(R.string.menu_amount_format, menu.amount)
                ioTvName.text = menu.name
                ioTvPrice.text = menu.total_price.toString()
            }
        }
    }

    inner class OrderWithNoteViewHolder(private val binding: ItemOrderWithNoteBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: Menu) {
            with(binding) {
                iownTvAmount.text = itemView.context.resources.getString(R.string.menu_amount_format, menu.amount)
                iownTvName.text = menu.name
                iownTvPrice.text = menu.total_price.toString()
                iownTvNote.text = menu.note
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