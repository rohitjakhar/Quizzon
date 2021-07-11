package com.rohit.quizzon.data.remote

import com.rohit.quizzon.BuildConfig
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor : Interceptor {
    val username: String = BuildConfig.USERNAME
    val password: String = BuildConfig.PASSWORD
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.request()
        val authRequest = response.newBuilder()
            .addHeader("Authorization", Credentials.basic(username, password))
            .build()
        return chain.proceed(authRequest)
    }
}
