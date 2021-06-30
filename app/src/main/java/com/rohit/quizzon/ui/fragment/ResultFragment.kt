package com.rohit.quizzon.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rohit.quizzon.data.model.QuizResult
import com.rohit.quizzon.databinding.FragmentResultBinding
import com.rohit.quizzon.utils.autoCleaned
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.round

@AndroidEntryPoint
class ResultFragment : Fragment() {

    private var binding: FragmentResultBinding by autoCleaned()
    private val args: ResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        updateUi(args.quizResult)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
        binding.button.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(resultModel: QuizResult) = binding.apply {
        textResultQuestion.text =
            "${resultModel.rightAnswer} right out of ${resultModel.totalQuestion}"
        val result = (resultModel.rightAnswer.toDouble() / resultModel.totalQuestion) * 100
        btnResult.text = "${round(result)}%"
    }
}
