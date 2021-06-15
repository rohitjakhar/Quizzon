package com.rohit.quizzon.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rohit.quizzon.databinding.FragmentQuizListBinding
import com.rohit.quizzon.utils.autoCleaned

class QuizListFragment : Fragment() {

    private var binding: FragmentQuizListBinding by autoCleaned()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizListBinding.inflate(inflater, container, false)

        return binding.root
    }
}
