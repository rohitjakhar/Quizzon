package com.rohit.quizzon.data.model

data class CreateQuestionData(
    val questionIndex: Int,
    val questionStatement: String,
    val option1: String,
    val option2: String,
    val option3: String,
    val option4: String,
    val answer: String
)
