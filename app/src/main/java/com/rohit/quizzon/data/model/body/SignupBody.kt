package com.rohit.quizzon.data.model.body

data class SignupBody(
    val operation: String = "add_user",
    val role: String = "user",
    val username: String,
    val password: String,
    val active: Boolean = true,
)
