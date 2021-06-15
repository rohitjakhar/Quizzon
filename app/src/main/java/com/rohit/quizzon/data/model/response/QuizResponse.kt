package com.rohit.quizzon.data.model.response
import com.google.gson.annotations.SerializedName


data class QuizResponse(
    @SerializedName("inserted_hashes")
    val insertedHashes: List<String>,
    @SerializedName("message")
    val message: String,
    @SerializedName("skipped_hashes")
    val skippedHashes: List<Any>
)