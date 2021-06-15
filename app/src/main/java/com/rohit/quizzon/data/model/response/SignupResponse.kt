package com.rohit.quizzon.data.model.response

import com.google.gson.annotations.SerializedName

data class SignupResponse(
    @SerializedName("error")
    val error: String,
    @SerializedName("message")
    val message: String
)
