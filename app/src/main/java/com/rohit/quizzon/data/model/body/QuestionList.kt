package com.rohit.quizzon.data.model.body

import com.google.gson.annotations.SerializedName

data class QuestionList(
    @SerializedName("category_id")
    val categoryId: String,
    @SerializedName("created_by")
    val createdBy: String,
    @SerializedName("creater_id")
    val createrId: String,
    @SerializedName("questions")
    val questions: List<Question>,
    @SerializedName("quiz_title")
    val quizTitle: String,
    @SerializedName("total_questions")
    val totalQuestions: Int
) {
    data class Question(
        @SerializedName("Answer")
        val answer: String,
        @SerializedName("index")
        val index: Int,
        @SerializedName("options")
        val options: List<String>,
        @SerializedName("question_statement")
        val questionStatement: String
    )
}
