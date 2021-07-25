package com.rohit.quizzon.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.rohit.quizzon.data.model.body.UserProfileBody
import com.rohit.quizzon.utils.PreferenceDataStore
import com.rohit.quizzon.utils.PreferenceDataStore.PreferenceKey.PREF_CHANGE_LANGUAGE
import com.rohit.quizzon.utils.PreferenceDataStore.PreferenceKey.PREF_FIRST_TIME
import com.rohit.quizzon.utils.PreferenceDataStore.PreferenceKey.PREF_LOGGED_IN
import com.rohit.quizzon.utils.PreferenceDataStore.PreferenceKey.PREF_USER_ID
import com.rohit.quizzon.utils.PreferenceDataStore.PreferenceKey.PREF_USER_NAME
import com.rohit.quizzon.utils.PreferenceDataStore.PreferenceKey.PREF_USE_EMAIL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStorePreferenceStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferenceDataStore {

    companion object {
        const val PREFS_NAME = "app-pref"
    }

    override val isLoggedIn: Flow<Boolean> =
        dataStore.data.map { it[PREF_LOGGED_IN] ?: false }

    override val userProfile: Flow<UserProfileBody>
        get() = dataStore.data.map {
            UserProfileBody(
                username = it[PREF_USER_NAME] ?: "",
                user_id = it[PREF_USER_ID] ?: "",
                userEmail = it[PREF_USE_EMAIL] ?: ""
            )
        }

    override val languageSelected: Flow<String>
        get() = dataStore.data.map { it[PREF_CHANGE_LANGUAGE] ?: "en" }

    override val isFirstTime: Flow<Boolean>
        get() = dataStore.data.map { it[PREF_FIRST_TIME] ?: false }

    override suspend fun saveUserData(user: UserProfileBody) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[PREF_USER_NAME] = user.username
            mutablePreferences[PREF_USE_EMAIL] = user.userEmail
            mutablePreferences[PREF_USER_ID] = user.user_id
        }
    }

    override suspend fun isLogin(isLogin: Boolean) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[PREF_LOGGED_IN] = isLogin
        }
    }

    override suspend fun changeLanguage(language: String) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[PREF_CHANGE_LANGUAGE] = language
        }
    }

    override suspend fun firstTime(isFirstTime: Boolean) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[PREF_FIRST_TIME] = isFirstTime
        }
    }

    override suspend fun clearData() {
        dataStore.edit { it.clear() }
    }
}
