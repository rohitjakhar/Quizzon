package com.rohit.quizzon.data.model.body

data class InsertDataBody(
    val operation: String,
    val schema: String,
    val table: String,
    val records: List<*>
)
