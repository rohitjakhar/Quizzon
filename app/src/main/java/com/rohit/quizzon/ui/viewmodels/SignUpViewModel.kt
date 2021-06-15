package com.rohit.quizzon.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohit.quizzon.data.DataStorePreferenceStorage
import com.rohit.quizzon.data.RemotRepository
import com.rohit.quizzon.data.model.body.SignupBody
import com.rohit.quizzon.data.model.body.TokenBody
import com.rohit.quizzon.data.model.response.SignupResponse
import com.rohit.quizzon.data.model.response.TokenResponse
import com.rohit.quizzon.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val remoteRepository: RemotRepository,
    private val dataStorePreferenceStorage: DataStorePreferenceStorage
) : ViewModel() {

    private var _Signupresponse: MutableStateFlow<NetworkResponse<SignupResponse>> =
        MutableStateFlow(NetworkResponse.Loading())
    val signupresponse get() = _Signupresponse

    private var _tokenResonse: MutableStateFlow<NetworkResponse<TokenResponse>> =
        MutableStateFlow(NetworkResponse.Loading())
    val tokenResponse get() = _tokenResonse

    suspend fun signup(
        username: String,
        password: String
    ) {
        _Signupresponse.value = remoteRepository.userSignUp(
            SignupBody(username = username, password = password)
        )
    }

    suspend fun createToken(
        username: String,
        password: String
    ) {
        _tokenResonse.value = remoteRepository.createToken(
            tokenBody = TokenBody(
                username = username,
                password = password
            )
        )
    }

    suspend fun saveToken(
        tokenResponse: TokenResponse
    ) {
        viewModelScope.launch {
            dataStorePreferenceStorage.addToken(tokenResponse)
        }
    }
}
