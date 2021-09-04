package com.rohit.quizzon.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohit.quizzon.data.model.CreateQuestionData
import com.rohit.quizzon.data.model.body.QuestionBody
import com.rohit.quizzon.data.model.body.UserProfileBody
import com.rohit.quizzon.data.model.response.CategoryResponseItem
import com.rohit.quizzon.data.model.response.DataInsertResponse
import com.rohit.quizzon.data.remote.RemoteRepository
import com.rohit.quizzon.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateQuizViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository
) : ViewModel() {

    private val _categoryList: MutableStateFlow<NetworkResponse<List<CategoryResponseItem>>> =
        MutableStateFlow(
            NetworkResponse.Loading()
        )
    val categoryList get() = _categoryList

    private var _uploadResponse: MutableStateFlow<NetworkResponse<DataInsertResponse>> =
        MutableStateFlow(NetworkResponse.Loading())
    val uploadResponse get() = _uploadResponse

    private val _questionList = MutableStateFlow<List<CreateQuestionData>>(mutableListOf())
    val questionList = _questionList.asStateFlow()

    var index = 0

    fun addQuestion(createQuestionData: CreateQuestionData) = viewModelScope.launch {
        val list = questionList.value.toMutableList()
        createQuestionData.questionIndex = index++
        list.add(createQuestionData)
        _questionList.emit(list)
    }

    fun removeQuestion(index: Int) = viewModelScope.launch {
        val list = questionList.value.toMutableList()
        list.removeAt(index)
        _questionList.emit(list)
    }

    private var _userProfile: MutableStateFlow<UserProfileBody> =
        MutableStateFlow(UserProfileBody("", "", ""))
    val userProfile get() = _userProfile

    fun getUserProfile() {
        viewModelScope.launch(IO) {
            _userProfile.value = remoteRepository.getUserProfile()
        }
    }

    fun uploadQuiz(quizBody: QuestionBody) {
        viewModelScope.launch(IO) {
            _uploadResponse.value = remoteRepository.uploadQuiz(quizBody)
        }
    }

    fun getCategoryList() {
        viewModelScope.launch(IO) {
            _categoryList.value = remoteRepository.categoryList()
        }
    }
}
