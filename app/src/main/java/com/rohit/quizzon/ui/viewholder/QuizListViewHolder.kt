package com.rohit.quizzon.ui.viewholder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rohit.quizzon.data.model.response.QuizResponse
import com.rohit.quizzon.databinding.SingleQuizBinding
import com.rohit.quizzon.utils.listener.QuizClickListener

class QuizListViewHolder(
    private val binding: SingleQuizBinding
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(quizListBody: QuizResponse, quizClickListener: QuizClickListener) = with(binding) {
        txtQuizTitle.text = quizListBody.quizTitle
        txtTotalQuiz.text = "Total Questions: ${quizListBody.totalQuestion}"
        textQuizCategory.text = "Category: ${quizListBody.categoryName}"
        textCreaterName.text = "Created By: ${quizListBody.createName}"
        startQuiz.setOnClickListener {
            quizClickListener.quizClickListener(quizListBody)
        }
    }

    companion object {
        fun create(parent: ViewGroup): QuizListViewHolder {
            val mView = LayoutInflater.from(parent.context)
            val binding = SingleQuizBinding.inflate(mView, parent, false)
            return QuizListViewHolder(binding = binding)
        }
    }
}
