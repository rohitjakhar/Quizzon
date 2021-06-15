package com.rohit.quizzon.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rohit.quizzon.data.model.CreateQuestionData
import com.rohit.quizzon.databinding.CreateQuestionSingleRowBinding

class CreateQuestionViewHolder(
    private val binding: CreateQuestionSingleRowBinding
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(questionData: CreateQuestionData) {
        binding.textQuestionIndex.text = questionData.questionIndex.toString()
        binding.textQuestionStatement.text = questionData.questionStatement
    }

    companion object {
        fun create(parent: ViewGroup): CreateQuestionViewHolder {
            val mView = LayoutInflater.from(parent.context)
            val binding = CreateQuestionSingleRowBinding.inflate(mView, parent, false)
            return CreateQuestionViewHolder(binding)
        }
    }
}
