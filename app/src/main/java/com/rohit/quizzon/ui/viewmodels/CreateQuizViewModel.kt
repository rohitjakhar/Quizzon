package com.rohit.quizzon.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohit.quizzon.data.model.body.QuestionBody
import com.rohit.quizzon.data.model.body.UserProfileBody
import com.rohit.quizzon.data.model.response.CategoryResponseItem
import com.rohit.quizzon.data.model.response.DataInsertResponse
import com.rohit.quizzon.data.remote.RemoteRepository
import com.rohit.quizzon.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
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
