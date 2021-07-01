package com.rohit.quizzon.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohit.quizzon.data.model.body.UserProfileBody
import com.rohit.quizzon.data.model.response.DeleteResponseBody
import com.rohit.quizzon.data.model.response.QuizResponse
import com.rohit.quizzon.data.remote.RemoteRepository
import com.rohit.quizzon.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository
) : ViewModel() {
    private var _userProfile: MutableStateFlow<UserProfileBody> =
        MutableStateFlow(UserProfileBody("", "", ""))
    val userProfile get() = _userProfile

    private var _quizList: MutableStateFlow<NetworkResponse<List<QuizResponse>>> =
        MutableStateFlow(NetworkResponse.Loading())
    val quizList get() = _quizList

    private var _deleteQuiz: MutableStateFlow<NetworkResponse<DeleteResponseBody>> =
        MutableStateFlow(NetworkResponse.Loading())
    val deleteQuizReponse get() = _deleteQuiz

    fun clearDataStore() {
        viewModelScope.launch {
            remoteRepository.clearDataStore()
        }
    }

    fun getUserProfile() {
        viewModelScope.launch {
            _userProfile.value = remoteRepository.getUserProfile()
        }
    }

    fun getUserQuizList(userId: String) {
        viewModelScope.launch {
            quizList.value = remoteRepository.userQuizList(userId)
        }
    }

    fun deleteQuiz(quizId: String) {
        viewModelScope.launch {
            deleteQuizReponse.value = remoteRepository.deleteQuiz(quizId)
        }
    }
}
