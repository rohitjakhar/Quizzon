package com.rohit.quizzon.data.model.body

import com.rohit.quizzon.data.model.CreateQuestionData

data class QuestionBody(
    val quiz_title: String,
    var create_id: String,
    val category_id: String,
    val category_name: String,
    var create_name: String,
    val question_list: List<CreateQuestionData>,
    val total_question: Int
)
