package com.rohit.quizzon.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rohit.quizzon.R
import com.rohit.quizzon.data.model.response.QuizResponse
import com.rohit.quizzon.databinding.SingleQuizBinding
import com.rohit.quizzon.utils.listener.QuizClickListener

class QuizListViewHolder(
    private val binding: SingleQuizBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(quizListBody: QuizResponse, quizClickListener: QuizClickListener) = with(binding) {
        val context = root.context
        txtQuizTitle.text = quizListBody.quizTitle
        txtTotalQuiz.text =
            context.getString(R.string.total_question, quizListBody.totalQuestion.toString())
        textQuizCategory.text = context.getString(R.string.category_name, quizListBody.categoryName)
        textCreaterName.text = context.getString(R.string.created_by, quizListBody.createName)
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
