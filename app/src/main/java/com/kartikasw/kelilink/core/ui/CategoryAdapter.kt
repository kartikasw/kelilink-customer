package com.kartikasw.kelilink.core.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kartikasw.kelilink.core.domain.model.Category
import com.kartikasw.kelilink.databinding.ItemCategoryBinding

class CategoryAdapter: RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    private var itemList = ArrayList<Category>()

    var onItemClick: ((Category) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(categories: ArrayList<Category>) {
        itemList.apply {
            clear()
            addAll(categories)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class MyViewHolder(private val binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(itemList[adapterPosition])
            }
        }

        fun bind(category: Category) {
            with(binding) {
                icIvCategory.setImageResource(category.icon)
                icTvName.text = category.name
            }
        }
    }

    override fun getItemCount(): Int = itemList.size
}