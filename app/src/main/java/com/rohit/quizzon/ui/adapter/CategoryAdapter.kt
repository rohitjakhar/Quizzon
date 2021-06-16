package com.rohit.quizzon.ui.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.rohit.quizzon.data.model.response.CategoryResponseItem
import com.rohit.quizzon.ui.viewholder.CategoryListViiewHolder
import com.rohit.quizzon.utils.CategoryClickListner

class CategoryAdapter(
    private val categoryClickListener: CategoryClickListner
) :
    PagingDataAdapter<CategoryResponseItem, CategoryListViiewHolder>(CategoryItemDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViiewHolder {
        return CategoryListViiewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CategoryListViiewHolder, position: Int) {
        getItem(position)?.let {
            Log.d("tetee", "list: $it")
            holder.bind(it, categoryClickListner = categoryClickListener)
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
