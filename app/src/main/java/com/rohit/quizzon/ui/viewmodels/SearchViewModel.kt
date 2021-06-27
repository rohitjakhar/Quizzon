package com.rohit.quizzon.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohit.quizzon.data.RemoteRepository
import com.rohit.quizzon.data.model.response.QuizResponse
import com.rohit.quizzon.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository
) : ViewModel() {
    private var _quizList: MutableStateFlow<NetworkResponse<List<QuizResponse>>> =
        MutableStateFlow(NetworkResponse.Loading())
    val quizList get() = _quizList

    fun getQuizList() {
        viewModelScope.launch {
            quizList.value = remoteRepository.quizList()
        }
    }
}
