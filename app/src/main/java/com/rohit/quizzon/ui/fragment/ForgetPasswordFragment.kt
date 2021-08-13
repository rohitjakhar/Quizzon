package com.rohit.quizzon.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.rohit.quizzon.R
import com.rohit.quizzon.databinding.FragmentForgetPasswordBinding
import com.rohit.quizzon.utils.action
import com.rohit.quizzon.utils.autoCleaned
import com.rohit.quizzon.utils.snack
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ForgetPasswordFragment : Fragment() {
    private var binding: FragmentForgetPasswordBinding by autoCleaned()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)
        initClickListener()
        binding.btnForgetEmail.setOnClickListener {
        }
        return binding.root
    }

    private fun initClickListener() = binding.apply {
        imgBackArrow.setOnClickListener {
            findNavController().navigateUp()
        }
        btnForgetEmail.setOnClickListener {
            val email = binding.forgerPasswordInputField.editText?.text.toString().trim()
            if (email.isNotEmpty() && email.length > 5) {
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                    if (it.isSuccessful) {
                        binding.root.snack("${it.result}") {
                            action(resources.getString(R.string.ok)) {}
                        }
                    } else {
                        binding.root.snack("Error:${it.exception?.localizedMessage}") {
                            action(resources.getString(R.string.ok)) {}
                        }
                    }
                }
            }
        }
    }
}
