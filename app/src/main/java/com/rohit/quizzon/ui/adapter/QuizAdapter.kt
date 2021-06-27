package com.rohit.quizzon.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.rohit.quizzon.data.model.response.QuizResponse
import com.rohit.quizzon.ui.viewholder.QuizListViewHolder
import com.rohit.quizzon.utils.listener.QuizClickListener

class QuizAdapter(
    private val quizClickListener: QuizClickListener
) :
    ListAdapter<QuizResponse, QuizListViewHolder>(CategoryItemDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizListViewHolder {
        return QuizListViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: QuizListViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, quizClickListener)
        }
    }

    companion object {
        val CategoryItemDiffUtil = object : DiffUtil.ItemCallback<QuizResponse>() {
            override fun areItemsTheSame(
                oldItem: QuizResponse,
                newItem: QuizResponse
            ): Boolean {
                return oldItem.quizId == newItem.quizId
            }

            override fun areContentsTheSame(
                oldItem: QuizResponse,
                newItem: QuizResponse
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
