package com.rohit.quizzon.utils

sealed class NetworkResponse<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?, message: String) : NetworkResponse<T>(data, message = message)
    class Failure<T>(message: String) : NetworkResponse<T>(message = message)
    class Loading<T>() : NetworkResponse<T>()
}
