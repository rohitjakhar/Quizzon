package com.rohit.quizzon.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.rohit.quizzon.BuildConfig
import com.rohit.quizzon.data.model.body.DataGetBody
import com.rohit.quizzon.data.model.body.InsertDataBody
import com.rohit.quizzon.data.model.body.QuestionBody
import com.rohit.quizzon.data.model.body.UserProfileBody
import com.rohit.quizzon.data.model.response.CategoryResponseItem
import com.rohit.quizzon.data.model.response.DataInsertResponse
import com.rohit.quizzon.data.model.response.DeleteResponseBody
import com.rohit.quizzon.data.model.response.QuizResponse
import com.rohit.quizzon.data.model.response.UserProfileResponse
import com.rohit.quizzon.utils.NetworkResponse
import com.rohit.quizzon.utils.mapToErrorResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.Credentials
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    private val apiCall: QuizService,
    private val dataStorePreferenceStorage: DataStorePreferenceStorage,
    private val firebaseAuth: FirebaseAuth
) {
    private val cred = Credentials.basic(
        username = BuildConfig.USERNAME,
        password = BuildConfig.PASSWORD
    )

    suspend fun loginUser(email: String, password: String) =
        flow<NetworkResponse<UserProfileResponse>> {
            emit(NetworkResponse.Loading())
            val response = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            response.user?.let { firebaseUser ->
                if (firebaseUser.isEmailVerified) {
                    val getProfileData = getUserProfileFromHarper(firebaseUser.uid)
                    if (getProfileData is NetworkResponse.Success) {
                        getProfileData.data?.let {
                            dataStorePreferenceStorage.saveUserData(
                                UserProfileBody(
                                    user_id = it.userId,
                                    username = it.username,
                                    userEmail = it.userEmail
                                )
                            )
                        }
                        emit(
                            NetworkResponse.Success(
                                data = getProfileData.data,
                                message = "Added"
                            )
                        )
                    } else if (getProfileData is NetworkResponse.Failure) {
                        logout()
                        emit(getProfileData)
                    }
                } else {
                    firebaseAuth.signOut()
                    firebaseUser.sendEmailVerification()
                    emit(NetworkResponse.Failure(message = "Please Verify Email First"))
                }
            } ?: emit(NetworkResponse.Failure("Failed"))
        }.catch {
            emit(NetworkResponse.Failure(it.localizedMessage))
        }

    private fun logout() = firebaseAuth.signOut()

    suspend fun getUserProfile() = withContext(IO) {
        dataStorePreferenceStorage.userProfile.first()
    }

    suspend fun registerUser(
        email: String,
        username: String,
        password: String,
        gender: String
    ) = flow<NetworkResponse<DataInsertResponse>> {
        emit(NetworkResponse.Loading())
        val response = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        response.user?.let {
            Log.d("userImage", it.photoUrl.toString())
            it.sendEmailVerification()
            val userProfileBody = UserProfileBody(
                user_id = it.uid,
                username = username,
                userEmail = email
            )
            val userInsertBody = InsertDataBody(
                operation = INSERT_OPERATION,
                schema = APP_SCHEMA,
                table = USER_TABLE,
                records = listOf(userProfileBody)
            )
            val harperResponse = saveUserProfileToHarper(userInsertBody)
            if (harperResponse is NetworkResponse.Success) {
                firebaseAuth.signOut()
                emit(
                    NetworkResponse.Success(
                        data = harperResponse.data,
                        message = harperResponse.message
                    )
                )
            } else if (harperResponse is NetworkResponse.Failure) {
                it.delete().await()
                emit(harperResponse)
            }
        } ?: emit(NetworkResponse.Failure("Error"))
    }.catch {
        firebaseAuth.signOut()
        emit(NetworkResponse.Failure(it.message.toString()))
    }

    private suspend fun saveUserProfileToHarper(user: InsertDataBody): NetworkResponse<DataInsertResponse> {
        val response = apiCall.saveUserData(token = cred, user = user)
        return try {
            if (response.isSuccessful) NetworkResponse.Success(
                data = response.body(),
                message = response.body()?.message
            ) else NetworkResponse.Failure(
                mapToErrorResponse(response.errorBody()).error
            )
        } catch (e: Exception) {
            NetworkResponse.Failure(e.localizedMessage)
        }
    }

    private suspend fun getUserProfileFromHarper(userId: String): NetworkResponse<UserProfileResponse> {
        val response = apiCall.getUserProfile(
            token = cred,
            DataGetBody(
                operation = SQL_OPERATION,
                sql = "$GET_USER_PROFILE_QUERY WHERE user_id = '$userId'"
            )
        )
        return if (response.isSuccessful) {
            NetworkResponse.Success(data = response.body()?.get(0), message = response.message())
        } else {
            NetworkResponse.Failure(mapToErrorResponse(response.errorBody()).error)
        }
    }

    suspend fun clearDataStore() = withContext(IO) { dataStorePreferenceStorage.clearData() }
    suspend fun quizList(
        category_id: String = ""
    ): NetworkResponse<List<QuizResponse>> {
        val SQL_QUERY = when {
            category_id.isEmpty() -> GET_QUIZ_QUERY
            else -> "$GET_QUIZ_QUERY WHERE category_id = '$category_id'"
        }
        val response =
            apiCall.getQuizzes(cred, DataGetBody(operation = SQL_OPERATION, sql = SQL_QUERY))
        return if (response.isSuccessful) {
            NetworkResponse.Success(data = response.body(), message = response.message())
        } else {
            NetworkResponse.Failure(mapToErrorResponse(response.errorBody()).error)
        }
    }

    suspend fun categoryList(): NetworkResponse<List<CategoryResponseItem>> {
        val response = apiCall.getCategory(
            cred,
            DataGetBody(
                operation = SQL_OPERATION,
                sql = GET_CATEGORY_QUERY
            )
        )
        return if (response.isSuccessful) {
            NetworkResponse.Success(data = response.body(), message = response.message())
        } else {
            NetworkResponse.Failure(mapToErrorResponse(response.errorBody()).error)
        }
    }

    suspend fun uploadQuiz(questionBody: QuestionBody): NetworkResponse<DataInsertResponse> {
        val response = apiCall.createQuiz(
            token = cred,
            quizBody = InsertDataBody(
                operation = INSERT_OPERATION,
                schema = APP_SCHEMA,
                table = QUIZ_TABLE,
                records = listOf(questionBody)
            )
        )
        return if (response.isSuccessful) {
            NetworkResponse.Success(data = response.body(), message = response.message())
        } else NetworkResponse.Failure(mapToErrorResponse(response.errorBody()).error)
    }

    suspend fun loadQuizData(quizId: String): NetworkResponse<QuizResponse> {
        val SQL_QUERY = "$GET_QUIZ_QUERY WHERE quiz_id = '$quizId'"
        val response =
            apiCall.getQuizzes(cred, DataGetBody(operation = SQL_OPERATION, sql = SQL_QUERY))
        return if (response.isSuccessful) {
            val quizData = response.body()
            return if (!quizData.isNullOrEmpty()) {
                NetworkResponse.Success(data = quizData[0], message = "Added")
            } else NetworkResponse.Failure("Quiz Not Found!. Please Check Quiz Id")
        } else {
            NetworkResponse.Failure(message = mapToErrorResponse(response.errorBody()).error)
        }
    }

    suspend fun userQuizList(
        user_id: String
    ): NetworkResponse<List<QuizResponse>> {
        val SQL_QUERY = "$GET_QUIZ_QUERY WHERE create_id = '$user_id'"
        val response =
            apiCall.getQuizzes(cred, DataGetBody(operation = SQL_OPERATION, sql = SQL_QUERY))
        return if (response.isSuccessful) {
            NetworkResponse.Success(data = response.body(), message = response.message())
        } else {
            NetworkResponse.Failure(mapToErrorResponse(response.errorBody()).error)
        }
    }

    suspend fun deleteQuiz(quizId: String): NetworkResponse<DeleteResponseBody> {
        val SQL_QUERY = "$DELETE_QUIZ_QUERY WHERE quiz_id = '$quizId'"
        val response =
            apiCall.deleteQuiz(cred, DataGetBody(operation = SQL_OPERATION, sql = SQL_QUERY))
        return if (response.isSuccessful) {
            NetworkResponse.Success(data = null, message = response.message())
        } else {
            NetworkResponse.Failure(mapToErrorResponse(response.errorBody()).error)
        }
    }

    companion object {
        const val SQL_OPERATION = "sql"
        const val GET_CATEGORY_QUERY = "SELECT * FROM dev.category"
        const val GET_QUIZ_QUERY = "SELECT * FROM dev.quizzes"
        const val DELETE_QUIZ_QUERY = "DELETE FROM dev.quizzes"
        const val GET_USER_PROFILE_QUERY = "SELECT * FROM dev.user_profile"
        const val INSERT_OPERATION = "insert"
        const val APP_SCHEMA = "dev"
        const val USER_TABLE = "user_profile"
        const val QUIZ_TABLE = "quizzes"
    }
}
