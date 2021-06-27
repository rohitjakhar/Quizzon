package com.rohit.quizzon.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohit.quizzon.data.DataStorePreferenceStorage
import com.rohit.quizzon.data.RemoteRepository
import com.rohit.quizzon.data.model.body.User
import com.rohit.quizzon.data.model.response.UserProfileResponse
import com.rohit.quizzon.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository
) : ViewModel() {

    private var _loginStatus: MutableStateFlow<NetworkResponse<UserProfileResponse>> =
        MutableStateFlow(NetworkResponse.Loading())
    val loginState get() = _loginStatus

    fun loginUser(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            remoteRepository.loginUser(email, password).collect {
                _loginStatus.value = it
            }
        }
    }
}
