package com.rohit.quizzon.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rohit.quizzon.BuildConfig
import com.rohit.quizzon.data.model.body.CategoryBody
import com.rohit.quizzon.data.model.body.QuizBody
import com.rohit.quizzon.data.model.body.SignupBody
import com.rohit.quizzon.data.model.body.TokenBody
import com.rohit.quizzon.data.model.response.CategoryResponseItem
import com.rohit.quizzon.data.model.response.QuizResponse
import com.rohit.quizzon.data.model.response.SignupResponse
import com.rohit.quizzon.data.model.response.TokenResponse
import com.rohit.quizzon.data.pagingsource.CategoryPagingSource
import com.rohit.quizzon.utils.NetworkResponse
import com.rohit.quizzon.utils.mapToErrorResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import okhttp3.Credentials
import javax.inject.Inject

private const val PAGE_SIZE = 10
private const val MAX_PAGE_SIZE = PAGE_SIZE + (PAGE_SIZE * 2)

class RemotRepository @Inject constructor(
    private val apiCall: QuizService,
    private val dataStorePreferenceStorage: DataStorePreferenceStorage,
) {
    suspend fun userSignUp(
        signupBody: SignupBody
    ): NetworkResponse<SignupResponse> = withContext(IO) {
        try {
            val username = BuildConfig.USERNAME
            val password = BuildConfig.PASSWORD
            val reponse = apiCall.signup(
                cred = Credentials.basic(username = username, password = password),
                signupBody
            )
            if (reponse.isSuccessful) {
                reponse.body()?.let {
                    val user = it.message ?: "er"
                    return@withContext NetworkResponse.Success(message = user, data = null)
                } ?: return@withContext NetworkResponse.Failure(message = reponse.message())
            } else {
                return@withContext NetworkResponse.Failure(mapToErrorResponse(reponse.errorBody()).error)
            }
        } catch (e: Exception) {
            return@withContext NetworkResponse.Failure("Exception : ${e.localizedMessage}")
        }
    }

    fun getCategory(): Flow<PagingData<CategoryResponseItem>> {
        val token = getOperationToken()
        Log.d("test121", token)
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                maxSize = MAX_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CategoryPagingSource(
                    apiCall,
                    CategoryBody(),
                    token
                )
            }
        ).flow
    }

    suspend fun uploadQuiz(quizBody: QuizBody): NetworkResponse<QuizResponse> {
        val operationToken = getOperationToken()
        val response = apiCall.createQuiz(
            token = operationToken,
            quizBody = quizBody
        )
        return when (response) {
            is NetworkResponse.Success<*> -> NetworkResponse.Success(
                response.body(),
                message = response.body()!!.message
            )
            is NetworkResponse.Failure<*> -> NetworkResponse.Failure(mapToErrorResponse(response.errorBody()).error)
            else -> NetworkResponse.Failure(mapToErrorResponse(response.errorBody()).error)
        }
    }

    private fun getOperationToken(): String {
        var operationToken = ""
        CoroutineScope(IO).launch {
            withContext(Dispatchers.Default) {
                dataStorePreferenceStorage.operationToken.collectLatest {
                    operationToken = it
                }
            }
        }
        return operationToken
    }

    suspend fun createToken(
        tokenBody: TokenBody
    ): NetworkResponse<TokenResponse> = withContext(IO) {
        try {
            val response = apiCall.createToke(tokenBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    // TODO save token in datastore
                    return@withContext NetworkResponse.Success(data = it, "Added")
                } ?: return@withContext NetworkResponse.Failure("Error")
            } else {
                return@withContext NetworkResponse.Failure(mapToErrorResponse(response.errorBody()).error)
            }
        } catch (e: Exception) {
            return@withContext NetworkResponse.Failure(message = "Error: ${e.localizedMessage}")
        }
    }
}
