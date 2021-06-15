package com.rohit.quizzon.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rohit.quizzon.data.model.response.TokenResponse
import kotlinx.coroutines.flow.Flow

interface PreferenceDataStore {

    val isLoggedIn: Flow<Boolean>
    val refreshToken: Flow<String>
    val operationToken: Flow<String>

    suspend fun isLogin(isLogin: Boolean)
    suspend fun addToken(tokenResponse: TokenResponse)

    object PreferenceKey {
        val PREF_LOGGED_IN = booleanPreferencesKey(PREFERENCE_IS_LOGIN)
        val userName = stringPreferencesKey(PREFERENCE_USER_NAME)
        val userEmail = stringPreferencesKey(PREFERENCE_USER_EMAIL)
        val PREF_OPERATION_TOKEN = stringPreferencesKey(PREFERENCE_OPERATION_TOKEN)
        val PREF_REFRESH_TOKEN = stringPreferencesKey(PREFERENCE_REFRESH_TOKEN)
    }

    companion object {
        const val PREFERENCE_USER_NAME = "userName"
        const val PREFERENCE_USER_EMAIL = "userEmail"
        const val PREFERENCE_IS_LOGIN = "isLogin"
        const val PREFERENCE_PROFILE_URL = "userProfileUrl"
        const val PREFERENCE_NAME = "dataStoreName"
        const val PREFERENCE_NEW_USER = "newUser"
        const val PREFERENCE_USER_UID = "userUid"
        const val PREFERENCE_OPERATION_TOKEN = "operation_token"
        const val PREFERENCE_REFRESH_TOKEN = "refresh_token"
    }
}
