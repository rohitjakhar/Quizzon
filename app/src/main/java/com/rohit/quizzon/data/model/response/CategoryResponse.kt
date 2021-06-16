package com.rohit.quizzon.data.model.response

import com.google.gson.annotations.SerializedName

data class CategoryResponseItem(
    @SerializedName("category_name")
    val categoryName: String,
    @SerializedName("__createdtime__")
    val createdtime: Long,
    @SerializedName("id")
    val id: Int,
    @SerializedName("quize_id")
    val quizeId: List<String>,
    @SerializedName("total_quiz")
    val totalQuiz: Int,
    @SerializedName("__updatedtime__")
    val updatedtime: Long
)
