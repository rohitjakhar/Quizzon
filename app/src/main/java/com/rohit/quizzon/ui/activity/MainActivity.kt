package com.rohit.quizzon.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.rohit.quizzon.R
import com.rohit.quizzon.data.local.DataStorePreferenceStorage
import com.rohit.quizzon.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var dataStoreRepository: DataStorePreferenceStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.home_container_view) as NavHostFragment
        val navController = fragmentManager.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.quizListFragment -> hideBottomBar()
                R.id.quizFragment -> hideBottomBar()
                R.id.nav_create_quiz -> hideBottomBar()
                R.id.resultFragment -> hideBottomBar()
                else -> showBottomBar()
            }
        }

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_all_quiz,
                R.id.nav_profile,
                R.id.nav_about,
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigationBar.setupWithNavController(navController)
    }

    private fun showBottomBar() {
        with(binding) {
            bottomNavigationBar.visibility = View.VISIBLE
            bottomNavigationBar.visibility = View.VISIBLE
        }
    }

    private fun hideBottomBar() {
        with(binding) {
            bottomNavigationBar.visibility = View.GONE
            bottomNavigationBar.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.home_container_view)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
