package com.rohit.quizzon.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rohit.quizzon.data.model.response.CategoryResponseItem
import com.rohit.quizzon.databinding.SingleCategoryBinding
import com.rohit.quizzon.utils.listener.CategoryClickListner

class CategoryListViiewHolder(
    private val binding: SingleCategoryBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        categoryResponseItem: CategoryResponseItem,
        categoryClickListner: CategoryClickListner
    ) {
        binding.textCategoryName.text = categoryResponseItem.categoryName

        binding.textCategoryName.setOnClickListener {
            categoryClickListner.onCategoryListner(categoryResponseItem.id)
        }
    }

    companion object {
        fun create(parent: ViewGroup): CategoryListViiewHolder {
            val mView = LayoutInflater.from(parent.context)
            val binding = SingleCategoryBinding.inflate(mView, parent, false)
            return CategoryListViiewHolder(binding = binding)
        }
    }
}
