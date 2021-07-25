package com.rohit.quizzon.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rohit.quizzon.R
import com.rohit.quizzon.data.model.QuizResult
import com.rohit.quizzon.data.model.response.QuizResponse
import com.rohit.quizzon.databinding.FragmentPlayQuizBinding
import com.rohit.quizzon.utils.autoCleaned
import com.rohit.quizzon.utils.shortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayQuizFragment : Fragment() {

    private var binding: FragmentPlayQuizBinding by autoCleaned()
    private val args: PlayQuizFragmentArgs by navArgs()
    private var currentPosition = 0
    private var rightAnswer = 0
    private var wrongAnswer = 0
    private lateinit var quizData: QuizResponse
    private var isCheckedAnswer = false

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().actionBar?.hide()
        binding = FragmentPlayQuizBinding.inflate(inflater, container, false)
        quizData = (args.quizData)
        updateView()
        binding.btnCheckAnswer.setOnClickListener {
            chekButton()
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun chekButton() {
        if (isCheckedAnswer) {
            isCheckedAnswer = false
            if (currentPosition + 1 == args.quizData.totalQuestion) {
                val quizResult = QuizResult(
                    rightAnswer = rightAnswer,
                    wrongAnswer = wrongAnswer,
                    totalQuestion = args.quizData.totalQuestion
                )
                findNavController().navigate(
                    PlayQuizFragmentDirections.actionQuizFragmentToResultFragment(
                        quizResult
                    )
                )
            } else {
                binding.apply {
                    textAnswerStatus.isVisible = false
                    textRightAnswer.isVisible = false
                    btnCheckAnswer.text = "Check Answer"
                    radioGroupOption.clearCheck()
                }
                updateView()
            }
        } else {
            if (binding.radioGroupOption.checkedRadioButtonId == -1) {
                shortToast("PLease Chose A Option")
            } else {
                viewLifecycleOwner.lifecycleScope.launch {
                    checkAnswer(args.quizData)
                }
                isCheckedAnswer = true
            }
        }
    }

    private fun checkAnswer(quizData: QuizResponse) {
        if (currentPosition + 1 != quizData.totalQuestion) {
            binding.btnCheckAnswer.text = "Next"
            val answer = quizData.questionList[currentPosition].answer
            val selectedAnswer =
                requireActivity().findViewById<RadioButton>(binding.radioGroupOption.checkedRadioButtonId)
            if (answer == selectedAnswer.text) {
                rightAnswer++
                binding.textAnswerStatus.isVisible = true
                binding.textAnswerStatus.text = "Right"
            } else {
                wrongAnswer++
                binding.apply {
                    textAnswerStatus.isVisible = true
                    binding.textAnswerStatus.text = resources.getString(R.string.wrong)
                    textRightAnswer.isVisible = true
                    textRightAnswer.text = "Right Answer: $answer"
                }
            }
        } else {
            binding.btnCheckAnswer.text = "Result"
        }
        currentPosition++
    }

    private fun updateView() {
        loadQuestionAnimation()
        binding.apply {
            textQuizTitle.text = quizData.quizTitle
            textQuestionStatement.text = quizData.questionList[currentPosition].questionStatement
            radioOption1.text = quizData.questionList[currentPosition].option1
            radioOption2.text = quizData.questionList[currentPosition].option2
            radioOption3.text = quizData.questionList[currentPosition].option3
            radioOption4.text = quizData.questionList[currentPosition].option4
        }
    }

    private fun loadQuestionAnimation() = binding.apply {
        radioOption1.animate()
            .alpha(1f)
            .duration = 400L
        radioOption2.animate()
            .alpha(1f)
            .duration = 400L
        radioOption3.animate()
            .alpha(1f)
            .duration = 400L
        radioOption4.animate()
            .alpha(1f)
            .duration = 400L
        btnCheckAnswer.animate()
            .alpha(1f)
            .duration = 400L
        textQuestionStatement.animate()
            .alpha(1f)
            .duration = 400L
    }
}
