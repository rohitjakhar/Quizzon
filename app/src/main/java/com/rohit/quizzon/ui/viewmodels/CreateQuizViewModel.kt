package com.rohit.quizzon.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.rohit.quizzon.data.RemotRepository
import com.rohit.quizzon.data.model.body.QuizBody
import com.rohit.quizzon.data.model.response.QuizResponse
import com.rohit.quizzon.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CreateQuizViewModel @Inject constructor(
    private val remotRepository: RemotRepository
) : ViewModel() {

    private var _uploadResponse: MutableStateFlow<NetworkResponse<QuizResponse>> =
        MutableStateFlow(NetworkResponse.Loading())
    val uploadResponse get() = _uploadResponse

    suspend fun uploadQuiz(quizBody: QuizBody) {
        _uploadResponse.value = remotRepository.uploadQuiz(quizBody)
    }
}
