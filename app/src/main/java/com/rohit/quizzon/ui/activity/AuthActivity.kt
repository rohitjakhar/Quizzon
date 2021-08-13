package com.rohit.quizzon.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.rohit.quizzon.R
import com.rohit.quizzon.databinding.ActivityAuthBinding
import com.rohit.quizzon.utils.LocalHelper
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.loginFragment,
                R.id.signupFragment
            )
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(binding.authFragmentContainer.id)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
