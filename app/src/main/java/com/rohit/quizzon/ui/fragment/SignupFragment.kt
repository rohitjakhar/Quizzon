package com.rohit.quizzon.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.rohit.quizzon.R
import com.rohit.quizzon.data.DataStorePreferenceStorage
import com.rohit.quizzon.databinding.FragmentSignupBinding
import com.rohit.quizzon.ui.viewmodels.SignUpViewModel
import com.rohit.quizzon.utils.NetworkResponse
import com.rohit.quizzon.utils.autoCleaned
import com.rohit.quizzon.utils.shortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
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

        val signupText = resources.getString(R.string.login_line)
        val spanableString = SpannableStringBuilder(signupText)

        val signUpClick = object : ClickableSpan() {
            override fun onClick(widget: View) {
                findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToLoginFragment())
            }
        }
        spanableString.setSpan(
            signUpClick,
            25,
            (signupText.length),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spanableString.setSpan(
            object : ForegroundColorSpan(Color.parseColor("#00AB5C")) {},
            25,
            (signupText.length),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.textLogin.movementMethod = LinkMovementMethod.getInstance()
        binding.textLogin.setText(spanableString, TextView.BufferType.SPANNABLE)
        binding.btnUserSignup.setOnClickListener {
            val userName = binding.userNameInputLayout.editText?.text.toString().trim()
            val userEmail = binding.userEmailInputLayout.editText?.text.toString().trim()
            val password = binding.userPasswordInputLayout.editText?.text.toString().trim()
            val confirmPassword =
                binding.userConfirmPasswordInputLayout.editText?.text.toString().trim()
            if (validateUserInput(
                    userName,
                    userEmail,
                    password,
                    confirmPassword
                )
            ) signupViewModel.registerUser(
                username = userName,
                userEmail = userEmail,
                userPassword = password
            )
            checkStatus()
        }
        return binding.root
    }

    private fun checkStatus() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            signupViewModel.registerState.collectLatest { value ->
                when (value) {
                    is NetworkResponse.Success -> {
                        shortToast("We sent verification link to your email")
                        findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToLoginFragment())
                    }
                    is NetworkResponse.Failure -> {
                        shortToast("Error: ${value.message}")
                    }
                    is NetworkResponse.Loading -> {
                        shortToast("Loading")
                    }
                }
            }
        }
    }

    private fun validateUserInput(
        userName: String,
        userEmail: String,
        userPassword: String,
        userConfirmPassword: String
    ): Boolean {
        return when {
            userEmail.length < 6 -> {
                binding.userEmailInputLayout.error = "Enter your Email"
                false
            }
            userName.length < 4 -> {
                binding.userNameInputLayout.error = "Enter your Name"
                false
            }
            userPassword != userConfirmPassword -> {
                binding.userConfirmPasswordInputLayout.error = "Password Not Match"
                return false
            }
            userPassword.length < 4 -> {
                binding.userPasswordInputLayout.error = "Enter your Password"
                false
            }
            else -> true
        }
    }
}
