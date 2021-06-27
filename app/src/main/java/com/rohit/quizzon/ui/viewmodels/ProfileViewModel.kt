package com.rohit.quizzon.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohit.quizzon.data.RemoteRepository
import com.rohit.quizzon.data.model.body.UserProfileBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository
) : ViewModel() {
    private var _userProfile: MutableStateFlow<UserProfileBody> =
        MutableStateFlow(UserProfileBody("", "", "", ""))
    val userProfile get() = _userProfile

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
}
