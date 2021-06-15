package com.rohit.quizzon.data.model.body

data class UserProfileBody(
    val userid: String,
    val username: String,
    val userEmail: String,
    val userDOB: String,
    val gender: String,
)
