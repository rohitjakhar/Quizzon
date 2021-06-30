package com.rohit.quizzon.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.rohit.quizzon.MainActivity
import com.rohit.quizzon.databinding.FragmentLoginBinding
import com.rohit.quizzon.ui.viewmodels.LoginViewModel
import com.rohit.quizzon.utils.NetworkResponse
import com.rohit.quizzon.utils.action
import com.rohit.quizzon.utils.autoCleaned
import com.rohit.quizzon.utils.snack
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var binding: FragmentLoginBinding by autoCleaned()
    private val loginViewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        initViewForProgress()
        initClickListener()

        binding.textForgetPassword.setOnClickListener {
            val ac = LoginFragmentDirections.actionLoginFragmentToForgetPasswordFragment()
            findNavController().navigate(ac)
        }
        binding.textRegisterAccount.setOnClickListener {
            val loginToSignup = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
            findNavController().navigate(loginToSignup)
        }

        binding.fabBackButton.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

    private fun initClickListener() = binding.apply {
        btnUserLogin.setOnClickListener {
            val email = binding.userEmailInputLayout.editText?.text.toString().trim()
            val password = binding.userPasswordInputLayout.editText?.text.toString().trim()
            if (validateInputs(email, password)) {
                btnUserLogin.activate()
                loginViewModel.loginUser(email, password)
                checkLoginState()
            }
        }
    }

    private fun initViewForProgress() {
        binding.apply {
            btnUserLogin.setDisableViews(
                listOf(
                    userEmailInputLayout,
                    userPasswordInputLayout,
                    textForgetPassword,
                    textRegisterAccount,
                    fabBackButton
                )
            )
        }
    }

    private fun validateInputs(
        userEmail: String,
        userPassword: String
    ): Boolean {
        return when {
            userEmail.length < 4 -> {
                binding.userEmailInputLayout.error = "Enter Email"
                false
            }
            userPassword.length < 4 -> {
                binding.userPasswordInputLayout.error = "Enter Password"
                false
            }
            else -> true
        }
    }

    private fun checkLoginState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            loginViewModel.loginState.collectLatest { value ->
                when (value) {
                    is NetworkResponse.Success -> {
                        binding.root.snack("Login Success") {
                        }
                        requireActivity().apply {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                    }
                    is NetworkResponse.Failure -> {
                        binding.btnUserLogin.reset()
                        binding.root.snack("${value.message}") {
                            action("Ok") {
                            }
                        }
                    }
                    is NetworkResponse.Loading -> {
                        binding.btnUserLogin.activate()
                    }
                }
            }
        }
    }
}
