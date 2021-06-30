package com.rohit.quizzon.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.rohit.quizzon.data.model.CreateQuestionData
import com.rohit.quizzon.ui.viewholder.CreateQuestionViewHolder
import com.rohit.quizzon.utils.listener.CreateQuizListener

class QuestionAdapter(
    private val createQuizListener: CreateQuizListener
) :
    ListAdapter<CreateQuestionData, CreateQuestionViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateQuestionViewHolder {
        return CreateQuestionViewHolder.create(parent, createQuizListener)
    }

    override fun onBindViewHolder(holder: CreateQuestionViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
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
