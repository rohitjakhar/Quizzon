package com.rohit.quizzon.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.rohit.quizzon.MainActivity
import com.rohit.quizzon.data.DataStorePreferenceStorage
import com.rohit.quizzon.data.model.response.TokenResponse
import com.rohit.quizzon.databinding.FragmentSignupBinding
import com.rohit.quizzon.ui.viewmodels.SignUpViewModel
import com.rohit.quizzon.utils.NetworkResponse
import com.rohit.quizzon.utils.autoCleaned
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private var binding: FragmentSignupBinding by autoCleaned()
    private val signupViewModel: SignUpViewModel by viewModels()
    @Inject
    lateinit var dataStoreRepository: DataStorePreferenceStorage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        binding.btnUserSignup.setOnClickListener {
            validateUserInput(
                binding.userNameInputLayout.editText?.text.toString().trim(),
                binding.userEmailInputLayout.editText?.text.toString().trim(),
                binding.userPasswordInputLayout.editText?.text.toString().trim()
            )
        }
        return binding.root
    }

    private fun validateUserInput(userName: String, userEmail: String, userPassword: String) {
        if (userEmail.length < 6) {
            binding.userEmailInputLayout.error = "Enter your Email"
            return
        }
        if (userName.length < 4) {
            binding.userNameInputLayout.error = "Enter your Name"
            return
        }
        if (userPassword.length < 4) {
            binding.userPasswordInputLayout.error = "Enter your Password"
            return
        }
        gotoSignUp(
            userName,
            userEmail,
            userPassword
        )
    }

    private fun gotoSignUp(userName: String, userEmail: String, userPassword: String) {
        lifecycleScope.launch {
            signupViewModel.signup(
                userEmail,
                userPassword
            )
            signupViewModel.signupresponse.collectLatest {
                when (it) {
                    is NetworkResponse.Success -> {
                        createToken(userEmail, userPassword)
                    }
                    is NetworkResponse.Failure -> {
                        Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_LONG)
                            .show()
                    }
                    else -> {
                        Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }

    private fun createToken(userEmail: String, userPassword: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            signupViewModel.createToken(
                userEmail,
                userPassword
            )
            signupViewModel.tokenResponse.collectLatest { value: NetworkResponse<TokenResponse> ->
                when (value) {
                    is NetworkResponse.Success -> {
                        signupViewModel.saveToken(value.data!!)
                        requireActivity().apply {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                    }
                    is NetworkResponse.Failure -> {
                        Toast.makeText(
                            requireContext(),
                            "Error: ${value.message}",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                    else -> {
                        Toast.makeText(
                            requireContext(),
                            "Error: ${value.message}",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            }
        }
    }
}
