package com.rohit.quizzon.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rohit.quizzon.MainActivity
import com.rohit.quizzon.data.DataStorePreferenceStorage
import com.rohit.quizzon.databinding.ActivitySplashScrenBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScrenBinding

    @Inject
    lateinit var dataStorePreferenceStorage: DataStorePreferenceStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScrenBinding.inflate(layoutInflater)
        checkUserLogin()
    }

    private fun checkUserLogin() {
        lifecycleScope.launchWhenStarted {
            dataStorePreferenceStorage.isLoggedIn.collect { isLogin ->
                if (isLogin) {
                    startNextActivity(MainActivity::class.java)
                } else {
                    startNextActivity(AuthActivity::class.java)
                }
            }
        }
    }

    private fun startNextActivity(activity: Class<*>) {
        startActivity(Intent(this, activity))
        finish()
    }
}
