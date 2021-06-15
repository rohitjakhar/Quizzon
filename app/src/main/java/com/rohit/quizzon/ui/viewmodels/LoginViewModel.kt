package com.rohit.quizzon.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohit.quizzon.data.DataStorePreferenceStorage
import com.rohit.quizzon.data.RemotRepository
import com.rohit.quizzon.data.model.body.TokenBody
import com.rohit.quizzon.data.model.response.TokenResponse
import com.rohit.quizzon.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val remoteRepository: RemotRepository,
    private val dataStorePreferenceStorage: DataStorePreferenceStorage
) : ViewModel() {

    private var _tokenResonse: MutableStateFlow<NetworkResponse<TokenResponse>> =
        MutableStateFlow(NetworkResponse.Loading())
    val tokenResponse get() = _tokenResonse

    suspend fun loginUser(
        username: String,
        password: String
    ) {
        _tokenResonse.value = remoteRepository.createToken(
            TokenBody(
                username = username,
                password = password
            )
        )
    }

    suspend fun saveToken(
        tokenResponse: TokenResponse
    ) {
        viewModelScope.launch {
            dataStorePreferenceStorage.isLogin(true)
            dataStorePreferenceStorage.addToken(tokenResponse)
        }
    }
}
