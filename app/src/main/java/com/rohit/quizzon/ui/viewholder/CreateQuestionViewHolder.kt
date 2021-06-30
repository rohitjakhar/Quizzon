package com.rohit.quizzon.ui.viewholder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rohit.quizzon.data.model.CreateQuestionData
import com.rohit.quizzon.databinding.SingleCreateQuestionBinding
import com.rohit.quizzon.utils.listener.CreateQuizListener

class CreateQuestionViewHolder(
    private val binding: SingleCreateQuestionBinding,
    private val createQuizListener: CreateQuizListener
) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(questionData: CreateQuestionData) = binding.apply {
        textQuestionIndex.text = questionData.questionIndex.toString()
        textQuestionStatement.text = questionData.questionStatement
        textOption1.text = "Option 1: ${questionData.option1}"
        textOption2.text = "Option 2: ${questionData.option2}"
        textOption3.text = "Option 3: ${questionData.option3}"
        textOption4.text = "Option 4: ${questionData.option4}"
        textAnswer.text = "Answer: ${questionData.answer}"
        imgDeleteItem.setOnClickListener {
            createQuizListener.clickDeleteItem(absoluteAdapterPosition)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            createQuizListener: CreateQuizListener
        ): CreateQuestionViewHolder {
            val mView = LayoutInflater.from(parent.context)
            val binding = SingleCreateQuestionBinding.inflate(mView, parent, false)
            return CreateQuestionViewHolder(binding, createQuizListener)
        }
    }
}
