package com.rohit.quizzon.utils

import com.google.gson.Gson
import com.rohit.quizzon.data.model.response.ErrorResponse
import okhttp3.ResponseBody

fun mapToErrorResponse(errorBody: ResponseBody?): ErrorResponse {
    val gson = Gson()
    return gson.fromJson(
        errorBody?.charStream(),
        ErrorResponse::class.java
    )
}
