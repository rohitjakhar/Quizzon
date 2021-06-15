package com.rohit.quizzon.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rohit.quizzon.databinding.QuizSingleRowBinding

class QuizListViewHolder(
    private val binding: QuizSingleRowBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind() {
    }

    companion object {
        fun create(parent: ViewGroup): QuizListViewHolder {
            val mView = LayoutInflater.from(parent.context)
            val binding = QuizSingleRowBinding.inflate(mView, parent, false)
            return QuizListViewHolder(binding = binding)
        }
    }
}
