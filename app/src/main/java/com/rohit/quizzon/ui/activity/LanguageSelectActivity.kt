package com.rohit.quizzon.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rohit.quizzon.data.local.DataStorePreferenceStorage
import com.rohit.quizzon.databinding.ActivityLanguageSelectBinding
import com.rohit.quizzon.utils.Config.Companion.currentLanguage
import com.rohit.quizzon.utils.LocalHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LanguageSelectActivity : AppCompatActivity() {
    @Inject
    lateinit var dataStorePreferenceStorage: DataStorePreferenceStorage

    private lateinit var binding: ActivityLanguageSelectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSanskrit.setOnClickListener {
            changeLanguage("sa")
        }
        binding.btnEnglish.setOnClickListener {
            changeLanguage("en")
        }
    }

    private fun changeLanguage(language: String) {
        LocalHelper.setAppLocale(this, language)
        lifecycleScope.launchWhenStarted {
            dataStorePreferenceStorage.firstTime(false)
            dataStorePreferenceStorage.changeLanguage(language)
        }
        currentLanguage = language
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
}
