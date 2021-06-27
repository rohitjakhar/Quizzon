package com.rohit.quizzon.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohit.quizzon.data.RemoteRepository
import com.rohit.quizzon.data.model.response.CategoryResponseItem
import com.rohit.quizzon.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository
) : ViewModel() {

    private var _categoryList: MutableStateFlow<NetworkResponse<List<CategoryResponseItem>>> =
        MutableStateFlow(
            NetworkResponse.Loading()
        )
    val categoryList get() = _categoryList
    fun getCategoryList() {
        viewModelScope.launch {
            _categoryList.value = remoteRepository.categoryList()
        }
    }
}
