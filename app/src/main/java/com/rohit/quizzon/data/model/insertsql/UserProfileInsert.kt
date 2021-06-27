package com.rohit.quizzon.data.model.insertsql

import com.rohit.quizzon.data.model.body.UserProfileBody

data class UserProfileInsert(
    val operation: String = "insert",
    val schema: String = "quiz",
    val table: String = "user_profile",
    val records: List<UserProfileBody>
)
