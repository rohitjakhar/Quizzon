package com.rohit.quizzon.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.rohit.quizzon.data.local.DataStorePreferenceStorage
import com.rohit.quizzon.databinding.ActivitySplashScrenBinding
import com.rohit.quizzon.ui.activity.AuthActivity
import com.rohit.quizzon.ui.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScrenBinding

    @Inject
    lateinit var dataStorePreferenceStorage: DataStorePreferenceStorage
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScrenBinding.inflate(layoutInflater)
        checkUserLogin()
    }

    private fun checkUserLogin() {
        if (firebaseAuth.currentUser != null) {
            startNextActivity(MainActivity::class.java)
        } else {
            startNextActivity(AuthActivity::class.java)
        }
    }

    private fun startNextActivity(activity: Class<*>) {
        startActivity(Intent(this, activity))
        finish()
    }
}
