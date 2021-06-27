package com.rohit.quizzon.data.model.response

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    @SerializedName("__createdtime__")
    val createdtime: Long,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("__updatedtime__")
    val updatedtime: Long,
    @SerializedName("userEmail")
    val userEmail: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("userid")
    val userid: String,
    @SerializedName("username")
    val username: String
)
