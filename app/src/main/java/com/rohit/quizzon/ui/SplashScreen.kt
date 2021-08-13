package com.rohit.quizzon.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.*
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.auth.FirebaseAuth
import com.rohit.quizzon.data.local.DataStorePreferenceStorage
import com.rohit.quizzon.databinding.ActivitySplashScrenBinding
import com.rohit.quizzon.ui.activity.AuthActivity
import com.rohit.quizzon.ui.activity.LanguageSelectActivity
import com.rohit.quizzon.ui.activity.MainActivity
import com.rohit.quizzon.utils.Config.Companion.currentLanguage
import com.rohit.quizzon.utils.LocalHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import java.util.*
import javax.inject.Inject

const val UPDATE_REQUEST_CODE = 524

@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScrenBinding
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultLauncher ->
            if (resultLauncher.resultCode == RESULT_OK) {
            }
        }
    private val appUpdateManager: AppUpdateManager by lazy {
        AppUpdateManagerFactory.create(applicationContext)
    }

    @Inject
    lateinit var dataStorePreferenceStorage: DataStorePreferenceStorage

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScrenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkFirstTime()
    }

    private fun checkFirstTime() {
        lifecycleScope.launchWhenStarted {
            if (dataStorePreferenceStorage.isFirstTime.first()) {
                startNextActivity(LanguageSelectActivity::class.java)
                finish()
            } else {
                checkLanguage()
            }
        }
    }

    private suspend fun checkLanguage() {
        val languagge = dataStorePreferenceStorage.languageSelected.first()
        currentLanguage = languagge
        LocalHelper.setAppLocale(this@SplashScreen, currentLanguage)
        checkUpdate()
    }

    private fun checkUpdate() {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                appUpdateManager.startUpdateFlowForResult(
                    it,
                    this,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                        .setAllowAssetPackDeletion(true)
                        .build(),
                    UPDATE_REQUEST_CODE
                )
                resultLauncher.launch(intent)
            } else {
                checkUserLogin()
            }
        }
            .addOnFailureListener {
                checkUserLogin()
            }
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
