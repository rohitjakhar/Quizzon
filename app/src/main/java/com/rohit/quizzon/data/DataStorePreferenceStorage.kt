package com.rohit.quizzon.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.rohit.quizzon.data.model.response.TokenResponse
import com.rohit.quizzon.utils.PreferenceDataStore
import com.rohit.quizzon.utils.PreferenceDataStore.PreferenceKey.PREF_LOGGED_IN
import com.rohit.quizzon.utils.PreferenceDataStore.PreferenceKey.PREF_OPERATION_TOKEN
import com.rohit.quizzon.utils.PreferenceDataStore.PreferenceKey.PREF_REFRESH_TOKEN
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

    override val refreshToken: Flow<String>
        get() = dataStore.data.map {
            it[PREF_REFRESH_TOKEN] ?: ""
        }

    override val operationToken: Flow<String>
        get() = dataStore.data.map { it[PREF_OPERATION_TOKEN] ?: "" }

    override suspend fun isLogin(isLogin: Boolean) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[PREF_LOGGED_IN] = isLogin
        }
    }

    override suspend fun addToken(tokenResponse: TokenResponse) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[PREF_OPERATION_TOKEN] = tokenResponse.operationToken
            mutablePreferences[PREF_REFRESH_TOKEN] = tokenResponse.refreshToken
        }
    }
}
