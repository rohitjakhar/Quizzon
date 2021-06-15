package com.rohit.quizzon.data.model.response

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("operation_token")
    val operationToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
)
