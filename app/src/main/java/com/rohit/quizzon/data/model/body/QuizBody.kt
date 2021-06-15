package com.rohit.quizzon.data.model.body

import com.rohit.quizzon.data.model.CreateQuestionData

data class QuizBody(
    val quizeTitle: String,
    val create_id: String,
    val category_id: String,
    val category_name: String,
    val create_name: String,
    val questionlist: List<CreateQuestionData>
)
