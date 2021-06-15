package com.rohit.quizzon.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import com.rohit.quizzon.data.DataStorePreferenceStorage
import com.rohit.quizzon.data.DataStorePreferenceStorage.Companion.PREFS_NAME
import com.rohit.quizzon.data.QuizService
import com.rohit.quizzon.data.RemotRepository
import com.rohit.quizzon.utils.Config.Companion.BASE_URL
import com.rohit.quizzon.utils.PreferenceDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.collect
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val Context.datastore by preferencesDataStore(
        name = PREFS_NAME
    )

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttp(
        loggingInterceptor: HttpLoggingInterceptor
    ): Call.Factory {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .callTimeout(300, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        callFactory: Call.Factory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .callFactory(callFactory)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiCall(retrofit: Retrofit): QuizService = retrofit.create(QuizService::class.java)

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(PREFS_NAME)
        }
    }

    @Provides
    @Singleton
    fun providePreferenceStorage(@ApplicationContext context: Context): PreferenceDataStore =
        DataStorePreferenceStorage(context.datastore)

    @Provides
    @Singleton
    fun provideRemoteRepository(
        apiCall: QuizService,
        dataStorePreferenceStorage: DataStorePreferenceStorage
    ): RemotRepository = RemotRepository(
        apiCall = apiCall,
        dataStorePreferenceStorage = dataStorePreferenceStorage
    )
}
