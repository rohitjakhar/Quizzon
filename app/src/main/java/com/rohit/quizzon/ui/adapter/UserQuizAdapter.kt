package com.rohit.quizzon.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.rohit.quizzon.data.model.response.QuizResponse
import com.rohit.quizzon.ui.viewholder.UserQuizViewHolder
import com.rohit.quizzon.utils.listener.QuizClickListener

class UserQuizAdapter(private val quizClickListener: QuizClickListener) :
    ListAdapter<QuizResponse, UserQuizViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserQuizViewHolder {
        return UserQuizViewHolder.create(parent, quizClickListener)
    }

    override fun onBindViewHolder(holder: UserQuizViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<QuizResponse>() {
            override fun areContentsTheSame(oldItem: QuizResponse, newItem: QuizResponse): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: QuizResponse, newItem: QuizResponse): Boolean {
                return oldItem.quizId == newItem.quizId
            }
        }
    }
}
