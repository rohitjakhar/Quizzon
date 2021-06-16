package com.rohit.quizzon.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rohit.quizzon.data.model.response.CategoryResponseItem
import com.rohit.quizzon.databinding.CategorySingleRowBinding
import com.rohit.quizzon.utils.CategoryClickListner

class CategoryListViiewHolder(
    private val binding: CategorySingleRowBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        categoryResponseItem: CategoryResponseItem,
        categoryClickListner: CategoryClickListner
    ) {
        binding.textCategoryName.text = categoryResponseItem.categoryName

        binding.textCategoryName.setOnClickListener {
            categoryClickListner.onCategoryListner(categoryResponseItem.id.toString())
        }
    }

    companion object {
        fun create(parent: ViewGroup): CategoryListViiewHolder {
            val mView = LayoutInflater.from(parent.context)
            val binding = CategorySingleRowBinding.inflate(mView, parent, false)
            return CategoryListViiewHolder(binding = binding)
        }
    }
}
