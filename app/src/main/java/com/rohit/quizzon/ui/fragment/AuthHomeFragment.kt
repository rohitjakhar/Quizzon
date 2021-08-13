package com.rohit.quizzon.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.play.core.review.ReviewManagerFactory
import com.rohit.quizzon.databinding.FragmentAuthHomeBinding
import com.rohit.quizzon.utils.autoCleaned
import com.rohit.quizzon.utils.shortToast

class AuthHomeFragment : Fragment() {

    private var binding: FragmentAuthHomeBinding by autoCleaned()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthHomeBinding.inflate(inflater, container, false)

        binding.btnGoToLogin.setOnClickListener {
            findNavController().navigate(AuthHomeFragmentDirections.actionAuthHomeFragmentToLoginFragment())
        }
        binding.btnGoToSignUp.setOnClickListener {
            findNavController().navigate(AuthHomeFragmentDirections.actionAuthHomeFragmentToSignupFragment())
        }
        return binding.root
    }
}
