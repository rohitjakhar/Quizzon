package com.rohit.quizzon.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohit.quizzon.data.local.DataStorePreferenceStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStorePreferenceStorage: DataStorePreferenceStorage
) : ViewModel() {
    fun changeLanguage(language: String) = viewModelScope.launch {
        dataStorePreferenceStorage.changeLanguage(language)
    }
}
