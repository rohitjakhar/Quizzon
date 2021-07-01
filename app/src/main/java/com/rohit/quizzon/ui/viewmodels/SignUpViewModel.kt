package com.rohit.quizzon.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohit.quizzon.data.model.response.DataInsertResponse
import com.rohit.quizzon.data.remote.RemoteRepository
import com.rohit.quizzon.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository
) : ViewModel() {
    private var _registerState: MutableStateFlow<NetworkResponse<DataInsertResponse>> =
        MutableStateFlow(NetworkResponse.Loading())
    val registerState get() = _registerState

    fun registerUser(
        username: String,
        userEmail: String,
        userPassword: String
    ) {
        viewModelScope.launch {
            remoteRepository.registerUser(
                username = username,
                email = userEmail,
                password = userPassword
            ).collect { status ->
                _registerState.value = status
            }
        }
    }
}
