package com.rohit.quizzon.data.model.insertsql

import com.rohit.quizzon.data.model.body.QuestionBody

data class QuizInsertBody(
    val operation: String = "insert",
    val schema: String = "quiz",
    val table: String = "quizes",
    val records: List<QuestionBody>
)
