package com.rohit.quizzon.data

import com.rohit.quizzon.data.model.body.*
import com.rohit.quizzon.data.model.response.CategoryResponseItem
import com.rohit.quizzon.data.model.response.QuizResponse
import com.rohit.quizzon.data.model.response.SignupResponse
import com.rohit.quizzon.data.model.response.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface QuizService {

    @POST("/")
    suspend fun authorLogin(@Body login: LoginBody): TokenResponse

    @POST("/")
    suspend fun login(
        @Body login: LoginBody
    )

    @POST("/")
    suspend fun signup(
        @Header("Authorization") cred: String,
        @Body singup: SignupBody
    ): Response<SignupResponse>

    @POST("/")
    suspend fun createToke(
        @Body token: TokenBody
    ): Response<TokenResponse>

    @POST("/")
    suspend fun fetechQuizes()

    @POST("/")
    suspend fun fetchCategory(
        @Header("Authorization") token: String,
        @Body categoryBody: CategoryBody
    ): List<CategoryResponseItem>

    @POST("/")
    suspend fun fetchQuestions()

    @POST("/")
    suspend fun createQuiz(
        @Header("Authorization") token: String,
        @Body quizBody: QuizBody
    ): Response<QuizResponse>

    @POST("/")
    suspend fun fetchProfile()

    @POST("/")
    suspend fun refreshToken()
}
