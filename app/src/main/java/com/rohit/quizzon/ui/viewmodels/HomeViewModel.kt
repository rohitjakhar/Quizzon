package com.rohit.quizzon.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.rohit.quizzon.data.RemotRepository
import com.rohit.quizzon.data.model.response.CategoryResponseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val remoteRepository: RemotRepository
) : ViewModel() {

    private var _categoryList: MutableLiveData<PagingData<CategoryResponseItem>> =
        MutableLiveData()
    val categoryList get() = _categoryList

    suspend fun fetchCategory() {
        remoteRepository.getCategory().collectLatest {
            _categoryList.postValue(it)
            Log.d("test121", "size: $it")
        }
    }

    suspend fun recreateToken() {
        remoteRepository.recreateToken()
    }
}
