package com.rohit.quizzon.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rohit.quizzon.data.model.body.User
import com.rohit.quizzon.data.model.body.UserProfileBody
import com.rohit.quizzon.data.model.response.TokenResponse
import kotlinx.coroutines.flow.Flow

interface PreferenceDataStore {

    val isLoggedIn: Flow<Boolean>
    val userProfile: Flow<UserProfileBody>

    suspend fun isLogin(isLogin: Boolean)
    suspend fun saveUserData(user: UserProfileBody)
    suspend fun clearData()

    object PreferenceKey {
        val PREF_LOGGED_IN = booleanPreferencesKey(PREFERENCE_IS_LOGIN)
        val PREF_USER_NAME = stringPreferencesKey(PREFERENCE_USER_NAME)
        val PREF_USER_ID = stringPreferencesKey(PREFERENCE_USER_ID)
        val PREF_USE_EMAIL = stringPreferencesKey(PREFERENCE_USER_EMAIL)
        val PREF_USER_GENDER = stringPreferencesKey(PREFERENCE_USER_GENDER)
    }

    companion object {
        const val PREFERENCE_USER_NAME = "userName"
        const val PREFERENCE_USER_ID = "userId"
        const val PREFERENCE_USER_EMAIL = "userEmail"
        const val PREFERENCE_IS_LOGIN = "isLogin"
        const val PREFERENCE_USER_GENDER = "gender"
        const val PREFERENCE_NAME = "dataStoreName"
    }
}
