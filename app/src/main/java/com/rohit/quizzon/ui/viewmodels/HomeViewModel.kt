package com.rohit.quizzon.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.rohit.quizzon.data.RemotRepository
import com.rohit.quizzon.data.model.response.CategoryResponseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    remoteRepository: RemotRepository
) : ViewModel() {

    val listData: Flow<PagingData<CategoryResponseItem>> =
        remoteRepository.getCategory()
}
