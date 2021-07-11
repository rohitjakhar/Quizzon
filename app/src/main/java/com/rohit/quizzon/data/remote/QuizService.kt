package com.rohit.quizzon.data.remote

import com.rohit.quizzon.data.model.body.DataGetBody
import com.rohit.quizzon.data.model.body.InsertDataBody
import com.rohit.quizzon.data.model.response.CategoryResponseItem
import com.rohit.quizzon.data.model.response.DataInsertResponse
import com.rohit.quizzon.data.model.response.DeleteResponseBody
import com.rohit.quizzon.data.model.response.QuizResponse
import com.rohit.quizzon.data.model.response.UserProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface QuizService {

    @POST("/")
    suspend fun getQuizzes(
        @Body getQuizBody: DataGetBody
    ): Response<List<QuizResponse>>

    @POST("/")
    suspend fun deleteQuiz(
        @Body getQuizBody: DataGetBody
    ): Response<DeleteResponseBody>

    @POST("/")
    suspend fun saveUserData(
        @Body user: InsertDataBody
    ): Response<DataInsertResponse>

    @POST("/")
    suspend fun getCategory(
        @Body categoryBody: DataGetBody
    ): Response<List<CategoryResponseItem>>

    @POST("/")
    suspend fun getUserProfile(
        @Body categoryBody: DataGetBody
    ): Response<List<UserProfileResponse>>

    @POST("/")
    suspend fun createQuiz(
        @Body quizBody: InsertDataBody
    ): Response<DataInsertResponse>
}
