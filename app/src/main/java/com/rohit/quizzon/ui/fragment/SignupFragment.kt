package com.rohit.quizzon.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
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
import com.rohit.quizzon.utils.action
import com.rohit.quizzon.utils.autoCleaned
import com.rohit.quizzon.utils.snack
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
        initSpannableString()
        initClickListener()
        initView()
        return binding.root
    }

    private fun initView() = binding.apply {
        btnUserSignup.setDisableViews(
            listOf(
                userNameInputLayout,
                userConfirmPasswordInputLayout,
                userPasswordInputLayout,
                userEmailInputLayout,
                textLogin,
                fabBackButton
            )
        )
    }

    private fun initClickListener() {
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
            ) {
                binding.btnUserSignup.activate()
                signupViewModel.registerUser(
                    username = userName,
                    userEmail = userEmail,
                    userPassword = password
                )
                checkStatus()
            } else return@setOnClickListener
        }
    }

    private fun initSpannableString() {
        val signupText = resources.getString(R.string.login_line)
        val spannableStringBuilder = SpannableStringBuilder(signupText)
        val signUpClick = object : ClickableSpan() {
            override fun onClick(widget: View) {
                findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToLoginFragment())
            }
        }
        spannableStringBuilder.setSpan(
            signUpClick,
            25,
            (signupText.length),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableStringBuilder.setSpan(
            object : ForegroundColorSpan(Color.parseColor("#00AB5C")) {},
            25,
            (signupText.length),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.textLogin.movementMethod = LinkMovementMethod.getInstance()
        binding.textLogin.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE)
    }

    private fun checkStatus() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            signupViewModel.registerState.collectLatest { value ->
                when (value) {
                    is NetworkResponse.Success -> {
                        binding.btnUserSignup.finished()
                        binding.root.snack("We sent verification link to your email") {
                            action("Ok") {
                                findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToLoginFragment())
                            }
                        }
                        findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToLoginFragment())
                    }
                    is NetworkResponse.Failure -> {
                        binding.btnUserSignup.reset()
                        binding.root.snack("${value.message}") {
                            action("Ok") {
                            }
                        }
                    }
                    is NetworkResponse.Loading -> {
                        binding.btnUserSignup.activate()
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
