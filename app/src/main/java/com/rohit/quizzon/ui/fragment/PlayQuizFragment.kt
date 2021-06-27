package com.rohit.quizzon.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.rohit.quizzon.data.model.response.QuizResponse
import com.rohit.quizzon.databinding.FragmentPlayQuizBinding
import com.rohit.quizzon.utils.autoCleaned
import com.rohit.quizzon.utils.shortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayQuizFragment : Fragment() {

    private var binding: FragmentPlayQuizBinding by autoCleaned()
    private val args: PlayQuizFragmentArgs by navArgs()
    private var currentPosition = 0
    private var rightAnswer = 0
    private var wrongAnswer = 0
    private lateinit var quizData: QuizResponse
    private var selectedOptionText = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().actionBar?.hide()
        binding = FragmentPlayQuizBinding.inflate(inflater, container, false)
        quizData = (args.quizData)
        updateView()
        binding.optionChipGroup.setOnCheckedChangeListener { group, checkedId ->
            selectedOptionText = group.findViewById<Chip>(checkedId).text.trim().toString()
        }

        binding.btnCheckAnswer.setOnClickListener {
            if (selectedOptionText.isNotEmpty()) {
                viewLifecycleOwner.lifecycleScope.launch {
                    checkAnswer(args.quizData)
                }
            } else {
                shortToast("PLease Chose A Option")
            }
        }

        return binding.root
    }

    private fun finishQuestionAnimation() = binding.apply {
        chipOption1.animate()
            .alpha(0f)
            .duration = 400L
        chipOption2.animate()
            .alpha(0f)
            .duration = 400L
        chipOption3.animate()
            .alpha(0f)
            .duration = 400L
        chipOption4.animate()
            .alpha(0f)
            .duration = 400L
        btnCheckAnswer.animate()
            .alpha(0f)
            .duration = 400L
        textQuestionStatement.animate()
            .alpha(0f)
            .duration = 400L
    }

    private fun loadQuestionAnimation() = binding.apply {
        chipOption1.animate()
            .alpha(1f)
            .duration = 400L
        chipOption2.animate()
            .alpha(1f)
            .duration = 400L
        chipOption3.animate()
            .alpha(1f)
            .duration = 400L
        chipOption4.animate()
            .alpha(1f)
            .duration = 400L
        btnCheckAnswer.animate()
            .alpha(1f)
            .duration = 400L
        textQuestionStatement.animate()
            .alpha(1f)
            .duration = 400L
    }

    private suspend fun checkAnswer(quizData: QuizResponse) {
        val answer = quizData.questionList[currentPosition].answer
        if (answer == selectedOptionText) {
            rightAnswer++
            if (currentPosition + 1 != quizData.totalQuestion) {
                currentPosition++
                finishQuestionAnimation()
                delay(500)
                updateView()
            } else {
                // TODO Show Result Animation
                Log.d("test34", "Last Question")
            }
            // Count Right Answer and save 
            // Show Next Button
        } else {
            wrongAnswer++

            // Update chip: Right Answer stroke show in green and selected stroke in red
            // Show button Next
        }
    }

    private fun updateView() {
        loadQuestionAnimation()
        binding.apply {
            textQuestionStatement.text = quizData.questionList[currentPosition].questionStatement
            chipOption1.text = quizData.questionList[currentPosition].option1
            chipOption2.text = quizData.questionList[currentPosition].option2
            chipOption3.text = quizData.questionList[currentPosition].option3
            chipOption4.text = quizData.questionList[currentPosition].option4
        }
    }
}
