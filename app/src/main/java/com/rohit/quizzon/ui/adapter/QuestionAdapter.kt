package com.rohit.quizzon.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rohit.quizzon.data.model.CreateQuestionData
import com.rohit.quizzon.databinding.SingleCreateQuestionBinding

class QuestionAdapter(
    private val onDelete: (Int) -> Unit = {}
) :
    ListAdapter<CreateQuestionData, QuestionAdapter.CreateQuestionViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateQuestionViewHolder {
        return createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: CreateQuestionViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    private fun createViewHolder(
        parent: ViewGroup,
    ): CreateQuestionViewHolder {
        val mView = LayoutInflater.from(parent.context)
        val binding = SingleCreateQuestionBinding.inflate(mView, parent, false)
        return CreateQuestionViewHolder(binding)
    }

    inner class CreateQuestionViewHolder(
        private val binding: SingleCreateQuestionBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imgDeleteItem.setOnClickListener {
                onDelete(absoluteAdapterPosition)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(questionData: CreateQuestionData) = binding.apply {
            textQuestionIndex.text = questionData.questionIndex.toString()
            textQuestionStatement.text = questionData.questionStatement
            textOption1.text = "Option 1: ${questionData.option1}"
            textOption2.text = "Option 2: ${questionData.option2}"
            textOption3.text = "Option 3: ${questionData.option3}"
            textOption4.text = "Option 4: ${questionData.option4}"
            textAnswer.text = "Answer: ${questionData.answer}"
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CreateQuestionData>() {
            override fun areContentsTheSame(
                oldItem: CreateQuestionData,
                newItem: CreateQuestionData
            ): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(
                oldItem: CreateQuestionData,
                newItem: CreateQuestionData
            ): Boolean {
                return oldItem.questionIndex == newItem.questionIndex
            }
        }
    }
}
