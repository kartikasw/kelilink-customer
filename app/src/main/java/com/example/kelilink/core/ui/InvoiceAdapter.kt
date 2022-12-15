package com.example.kelilink.core.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.kelilink.R
import com.example.kelilink.core.domain.model.Invoice
import com.example.kelilink.databinding.ItemStoreQueueBinding
import com.example.kelilink.util.dateFormat
import com.example.kelilink.util.withCurrencyFormat

class InvoiceAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemList = ArrayList<Invoice>()

    var onItemClick: ((Invoice) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Invoice>?) {
        itemList.clear()

        if (items == null) {
            notifyDataSetChanged()
            return
        } else {
            itemList.addAll(items)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when(viewType) {
            QUEUE -> {
                val view =
                    ItemStoreQueueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                QueueViewHolder(view)
            }
            DONE -> {
                val view =
                    ItemStoreQueueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DoneViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is QueueViewHolder -> {
                holder.bind(itemList[position])
            }
            is DoneViewHolder -> {
                holder.bind(itemList[position])
            }
        }
    }

    inner class QueueViewHolder
        (private val binding: ItemStoreQueueBinding
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(itemList[adapterPosition])
            }
        }

        fun bind(invoice: Invoice) {
            val corner = itemView.context.resources.getDimensionPixelSize(R.dimen.corner_image)

            with(binding) {
                Glide.with(itemView.context)
                    .load(invoice.store_image)
                    .transform(CenterCrop(), RoundedCorners(corner))
                    .into(isqIvStore)

                isqTvTitle.text = invoice.store_name
                isqTvPrice.text = invoice.total_price.withCurrencyFormat()
                isqTvStatus.text = itemView.context.resources.getString(R.string.queue_format, invoice.queue_order)
                isqTvTime.text = dateFormat.format(invoice.time)
            }
        }

    }

    inner class DoneViewHolder
        (private val binding: ItemStoreQueueBinding
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(itemList[adapterPosition])
            }
        }

        fun bind(invoice: Invoice) {
            val corner = itemView.context.resources.getDimensionPixelSize(R.dimen.corner_image)

            with(binding) {
                Glide.with(itemView.context)
                    .load(invoice.store_image)
                    .transform(CenterCrop(), RoundedCorners(corner))
                    .into(isqIvStore)

                isqTvTitle.text = invoice.store_name
                isqTvPrice.text = invoice.total_price.withCurrencyFormat()
                isqTvStatus.visibility = ViewGroup.INVISIBLE
                isqTvTime.text = dateFormat.format(invoice.time)
            }
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int =
        when(itemList[position].status) {
            "cooking" -> QUEUE
            else -> DONE
        }

    companion object {
        const val QUEUE = 0
        const val DONE  = 1
    }

}