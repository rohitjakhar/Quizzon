package com.rohit.quizzon.data.model.response

import com.google.gson.annotations.SerializedName

data class DeleteResponseBody(
    @SerializedName("deleted_hashes")
    val deletedHashes: List<String>,
    @SerializedName("message")
    val message: String,
    @SerializedName("skipped_hashes")
    val skippedHashes: List<Any>
)
