package com.rohit.quizzon.data.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuizResponse(
    @SerializedName("quiz_id")
    val quizId: String,
    @SerializedName("category_id")
    val categoryId: String,
    @SerializedName("category_name")
    val categoryName: String,
    @SerializedName("category_name_sa")
    val categoryNameSanskrit: String,
    @SerializedName("create_id")
    val createId: String,
    @SerializedName("create_name")
    val createName: String,
    @SerializedName("question_list")
    val questionList: List<Question>,
    @SerializedName("quiz_title")
    val quizTitle: String,
    @SerializedName("total_question")
    val totalQuestion: Int
) : Parcelable {
    @Parcelize
    data class Question(
        @SerializedName("answer")
        val answer: String,
        @SerializedName("option1")
        val option1: String,
        @SerializedName("option2")
        val option2: String,
        @SerializedName("option3")
        val option3: String,
        @SerializedName("option4")
        val option4: String,
        @SerializedName("questionIndex")
        val questionIndex: Int,
        @SerializedName("questionStatement")
        val questionStatement: String
    ) : Parcelable
}
