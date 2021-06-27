package com.rohit.quizzon.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.rohit.quizzon.databinding.FragmentForgetPasswordBinding
import com.rohit.quizzon.utils.autoCleaned
import com.rohit.quizzon.utils.shortToast
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
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)

        binding.btnForgetEmail.setOnClickListener {
            val email = binding.forgerPasswordInputField.editText?.text.toString().trim()
            if (email.isNotEmpty() && email.length > 5) {
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                    if (it.isSuccessful) {
                        shortToast("Email sent check your inbox")
                    } else {
                        shortToast("Email not send: ${it.exception?.localizedMessage}")
                    }
                }
            }
        }
        return binding.root
    }
}
