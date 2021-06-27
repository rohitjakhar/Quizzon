package com.rohit.quizzon.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.rohit.quizzon.databinding.FragmentProfileBinding
import com.rohit.quizzon.ui.SplashScreen
import com.rohit.quizzon.ui.viewmodels.ProfileViewModel
import com.rohit.quizzon.utils.autoCleaned
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    private var binding: FragmentProfileBinding by autoCleaned()
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.btnUserLogout.setOnClickListener {
            firebaseAuth.signOut()
            profileViewModel.clearDataStore()
            requireActivity().apply {
                startActivity(Intent(this, SplashScreen::class.java))
                finish()
            }
        }
        profileViewModel.getUserProfile()
        updateView()
        return binding.root
    }

    private fun updateView() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            profileViewModel.userProfile.collectLatest {
                binding.apply {
                    this.textUserName.text = it.username
                    this.textUserEmail.text = it.userEmail
                }
            }
        }
    }
}
