package com.rohit.quizzon.data.model.body

data class CategoryBody(
    val operation: String = "sql",
    val sql: String = "SELECT * FROM quiz.category"
)