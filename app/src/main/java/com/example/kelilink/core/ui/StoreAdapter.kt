package com.example.kelilink.core.ui

import android.annotation.SuppressLint
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.kelilink.R
import com.example.kelilink.core.domain.model.Store
import com.example.kelilink.databinding.ItemStoreBinding
import com.example.kelilink.databinding.ItemStoreClosedBinding

class StoreAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemList = ArrayList<Store>()

    var onItemClick: ((Store) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Store>?) {
        itemList.clear()

        if (items == null) {
            notifyDataSetChanged()
            return
        } else {
            itemList.addAll(
                items.sortedBy { it.distance }
            )
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when(viewType) {
            OPEN -> {
                val view =
                    ItemStoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                OpenViewHolder(view)
            }
            CLOSED -> {
                val view =
                    ItemStoreClosedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ClosedViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is OpenViewHolder -> {
                holder.bind(itemList[position])
            }
            is ClosedViewHolder -> {
                holder.bind(itemList[position])
            }
        }
    }

    inner class OpenViewHolder(
        private val binding: ItemStoreBinding
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(itemList[adapterPosition])
            }
        }

        private val corner = itemView.context.resources.getDimensionPixelSize(R.dimen.corner_image)

        fun bind(store: Store) {
            var category = ""
            for(text in store.categories) {
                category = if(store.categories[store.categories.lastIndex] == text) {
                    "$category$text"
                } else {
                    "$category$text, "
                }
            }
            with(binding) {
                Glide.with(itemView.context)
                    .load(store.image)
                    .placeholder(R.drawable.shimmer_placeholder)
                    .transform(CenterCrop(), RoundedCorners(corner))
                    .into(isIvStore)

                isTvTitle.text = store.name
                isTvCategory.text = category
                isTvDistance.text = itemView.context.resources.getString(R.string.distance_format, String.format("% .1f", store.distance))
            }
        }
    }

    inner class ClosedViewHolder
        (private val binding: ItemStoreClosedBinding
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(itemList[adapterPosition])
            }
        }

        private val corner = itemView.context.resources.getDimensionPixelSize(R.dimen.corner_image)

        fun bind(store: Store) {
            var category = ""
            for(text in store.categories) {
                category = if(store.categories[store.categories.lastIndex] == text) {
                    "$category$text"
                } else {
                    "$category$text,"
                }
            }
            with(binding) {
                Glide.with(itemView.context)
                    .load(store.image)
                    .placeholder(R.drawable.shimmer_placeholder)
                    .transform(CenterCrop(), RoundedCorners(corner))
                    .into(iscIvStore)

                val colorMatrix = ColorMatrix()
                colorMatrix.setSaturation(0f)
                iscIvStore.colorFilter = ColorMatrixColorFilter(colorMatrix)

                iscTvTitle.text = store.name
                iscTvCategory.text = category
            }
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int =
        when(itemList[position].operating_status) {
            true -> OPEN
            false -> CLOSED
        }

    companion object {
        const val OPEN = 0
        const val CLOSED  = 1
    }

}