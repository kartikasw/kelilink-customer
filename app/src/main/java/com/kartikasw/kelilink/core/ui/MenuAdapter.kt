package com.kartikasw.kelilink.core.ui

import android.annotation.SuppressLint
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.kartikasw.kelilink.R
import com.kartikasw.kelilink.core.domain.model.Menu
import com.kartikasw.kelilink.databinding.ItemMenuNotSelectedBinding
import com.kartikasw.kelilink.databinding.ItemMenuOutOfStockBinding
import com.kartikasw.kelilink.databinding.ItemMenuSelectedBinding
import com.kartikasw.kelilink.databinding.ItemMenuSelectedWithNoteBinding


class MenuAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val NOT_SELECTED = 0
        const val SELECTED = 1
        const val SELECTED_WITH_NOTE = 2
        const val OUT_OF_STOCK = 3
    }

    private var itemList = ArrayList<Menu>()

    var onItemClick: ((Menu) -> Unit)? = null

    var onSelectClick: ((Menu) -> Unit)? = null

    var onIncreaseClick: ((Menu) -> Unit)? = null

    var onDecreaseClick: ((Menu) -> Unit)? = null

    var onEditNoteClick: ((Menu) -> Unit)? = null

    var onNoteClick: ((Menu) -> Unit)? = null

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
            SELECTED -> {
                val view =
                    ItemMenuSelectedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SelectedViewHolder(view)
            }
            SELECTED_WITH_NOTE -> {
                val view =
                    ItemMenuSelectedWithNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SelectedWithNoteViewHolder(view)
            }
            NOT_SELECTED -> {
                val view =
                    ItemMenuNotSelectedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                NotSelectedViewHolder(view)
            }
            OUT_OF_STOCK -> {
                val view =
                    ItemMenuOutOfStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                OutOfStockViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is SelectedViewHolder -> {
                holder.bind(itemList[position])
            }
            is SelectedWithNoteViewHolder -> {
                holder.bind(itemList[position])
            }
            is NotSelectedViewHolder -> {
                holder.bind(itemList[position])
            }
            is OutOfStockViewHolder -> {
                holder.bind(itemList[position])
            }
        }
    }

    inner class SelectedViewHolder(private val binding: ItemMenuSelectedBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            with(binding) {
                imsBtnAdd.setOnClickListener {
                    onIncreaseClick?.invoke(itemList[adapterPosition])
                }

                imsBtnMin.setOnClickListener {
                    onDecreaseClick?.invoke(itemList[adapterPosition])
                }

                imsBtnNote.setOnClickListener {
                    onNoteClick?.invoke(itemList[adapterPosition])
                }

                root.setOnClickListener {
                    onItemClick?.invoke(itemList[adapterPosition])
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(menu: Menu) {
            val corner = itemView.context.resources.getDimensionPixelSize(R.dimen.corner_image)
            with(binding) {
                Glide.with(itemView.context)
                    .load(menu.image)
                    .placeholder(R.drawable.shimmer_placeholder)
                    .transform(CenterCrop(), RoundedCorners(corner))
                    .into(imsIvMenu)

                imsTvTitle.text = menu.name
                imsTvPrice.text = "${menu.price}/${menu.unit}"
                imsTvAmount.text = menu.amount.toString()
            }
        }
    }

    inner class SelectedWithNoteViewHolder(
        private val binding: ItemMenuSelectedWithNoteBinding
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            with(binding) {
                imswnBtnAdd.setOnClickListener {
                    onIncreaseClick?.invoke(itemList[adapterPosition])
                }

                imswnBtnMinus.setOnClickListener {
                    onDecreaseClick?.invoke(itemList[adapterPosition])
                }

                imswnBtnNote.setOnClickListener {
                    onEditNoteClick?.invoke(itemList[adapterPosition])
                }

                root.setOnClickListener {
                    onItemClick?.invoke(itemList[adapterPosition])
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(menu: Menu) {
            val corner = itemView.context.resources.getDimensionPixelSize(R.dimen.corner_image)
            with(binding) {
                Glide.with(itemView.context)
                    .load(menu.image)
                    .placeholder(R.drawable.shimmer_placeholder)
                    .transform(CenterCrop(), RoundedCorners(corner))
                    .into(imswnIvMenu)

                imswnTvTitle.text = menu.name
                imswnTvPrice.text = "${menu.price}/${menu.unit}"
                imswnTvAmount.text = menu.amount.toString()
            }
        }
    }

    inner class NotSelectedViewHolder(
        private val binding: ItemMenuNotSelectedBinding
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imnsBtnAdd.setOnClickListener {
                onSelectClick?.invoke(itemList[adapterPosition])
            }

            binding.root.setOnClickListener {
                onItemClick?.invoke(itemList[adapterPosition])
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(menu: Menu) {
            val corner = itemView.context.resources.getDimensionPixelSize(R.dimen.corner_image)
            with(binding) {
                Glide.with(itemView.context)
                    .load(menu.image)
                    .placeholder(R.drawable.shimmer_placeholder)
                    .transform(CenterCrop(), RoundedCorners(corner))
                    .into(imnsIvMenu)

                imnsTvTitle.text = menu.name
                imnsTvPrice.text = "${menu.price}/${menu.unit}"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    inner class OutOfStockViewHolder(
        private val binding: ItemMenuOutOfStockBinding
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(itemList[adapterPosition])
            }
        }

        fun bind(menu: Menu) {
            val corner = itemView.context.resources.getDimensionPixelSize(R.dimen.corner_image)
            with(binding) {
                Glide.with(itemView.context)
                    .load(menu.image)
                    .placeholder(R.drawable.shimmer_placeholder)
                    .transform(CenterCrop(), RoundedCorners(corner))
                    .into(imoosIvMenu)

                val colorMatrix = ColorMatrix()
                colorMatrix.setSaturation(0f)
                imoosIvMenu.colorFilter = ColorMatrixColorFilter(colorMatrix)

                imoosTvTitle.text = menu.name
                imoosTvPrice.text = "${menu.price}/${menu.unit}"
            }
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int =
        when(itemList[position].available) {
            false -> OUT_OF_STOCK
            else -> {
                when(itemList[position].amount) {
                    0 -> NOT_SELECTED
                    else -> {
                        when(itemList[position].note) {
                            "" -> SELECTED
                            else -> SELECTED_WITH_NOTE
                        }
                    }
                }
            }
        }
}