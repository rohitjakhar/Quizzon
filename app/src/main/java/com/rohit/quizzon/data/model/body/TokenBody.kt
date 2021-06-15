package com.rohit.quizzon.data.model.body

data class TokenBody(
    val operation: String = "create_authentication_tokens",
    val username: String,
    val password: String
)
