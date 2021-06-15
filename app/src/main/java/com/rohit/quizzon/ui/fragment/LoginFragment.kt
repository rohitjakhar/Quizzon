package com.rohit.quizzon.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.rohit.quizzon.MainActivity
import com.rohit.quizzon.data.model.response.TokenResponse
import com.rohit.quizzon.databinding.FragmentLoginBinding
import com.rohit.quizzon.ui.viewmodels.LoginViewModel
import com.rohit.quizzon.utils.NetworkResponse
import com.rohit.quizzon.utils.autoCleaned
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var binding: FragmentLoginBinding by autoCleaned()
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.btnUserSignup.setOnClickListener {
            validateInputs(
                binding.userEmailInputLayout.editText?.text.toString().trim(),
                binding.userPasswordInputLayout.editText?.text.toString().trim()
            )
        }

        binding.textRegisterAccount.setOnClickListener {
            val loginToSignup = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
            findNavController().navigate(loginToSignup)
        }

        return binding.root
    }

    private fun validateInputs(
        userEmail: String,
        userPassword: String
    ) {
        if (userEmail.length < 4) {
            binding.userEmailInputLayout.error = "Enter Email"
            return
        }
        if (userPassword.length < 4) {
            binding.userPasswordInputLayout.error = "Enter Password"
            return
        }
        createToken(
            userEmail,
            userPassword
        )
    }

    private fun createToken(userEmail: String, userPassword: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            loginViewModel.loginUser(
                userEmail,
                userPassword
            )
            loginViewModel.tokenResponse.collectLatest { value: NetworkResponse<TokenResponse> ->
                when (value) {
                    is NetworkResponse.Success -> {
                        loginViewModel.saveToken(value.data!!)
                        Log.d("test22", "toek: ${value.data.operationToken}")
                        requireActivity().apply {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                    }
                    is NetworkResponse.Failure -> {
                        Toast.makeText(
                            requireContext(),
                            "${value.message}",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                    else -> {
                        Toast.makeText(
                            requireContext(),
                            "${value.message}",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            }
        }
    }
}
