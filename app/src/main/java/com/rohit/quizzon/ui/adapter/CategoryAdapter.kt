package com.rohit.quizzon.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.rohit.quizzon.data.model.response.CategoryResponseItem
import com.rohit.quizzon.ui.viewholder.CategoryListViiewHolder
import com.rohit.quizzon.utils.listener.CategoryClickListner

class CategoryAdapter(
    private val categoryClickListener: CategoryClickListner
) :
    ListAdapter<CategoryResponseItem, CategoryListViiewHolder>(CategoryItemDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViiewHolder {
        return CategoryListViiewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CategoryListViiewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, categoryClickListener = categoryClickListener)
        }
    }

    companion object {
        val CategoryItemDiffUtil = object : DiffUtil.ItemCallback<CategoryResponseItem>() {
            override fun areItemsTheSame(
                oldItem: CategoryResponseItem,
                newItem: CategoryResponseItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CategoryResponseItem,
                newItem: CategoryResponseItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
