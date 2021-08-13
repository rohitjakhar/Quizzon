package com.rohit.quizzon.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rohit.quizzon.data.model.response.CategoryResponseItem
import com.rohit.quizzon.databinding.SingleCategoryBinding
import com.rohit.quizzon.utils.Config.Companion.currentLanguage
import com.rohit.quizzon.utils.listener.CategoryClickListner
import java.util.Locale

class CategoryListViiewHolder(
    private val binding: SingleCategoryBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        categoryResponseItem: CategoryResponseItem,
        categoryClickListener: CategoryClickListner
    ) {
        binding.textCategoryName.text = when (currentLanguage) {
            "sa" -> {
                categoryResponseItem.categoryNameSanskrit
            }
            else -> {
                categoryResponseItem.categoryName
            }
        }

        binding.textCategoryName.setOnClickListener {
            categoryClickListener.onCategoryListner(categoryResponseItem.id)
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
