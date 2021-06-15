package com.rohit.quizzon.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rohit.quizzon.R
import com.rohit.quizzon.data.model.CreateQuestionData
import com.rohit.quizzon.data.model.body.QuizBody
import com.rohit.quizzon.databinding.FragmentCreateQuizBinding
import com.rohit.quizzon.databinding.QuestionDialogBinding
import com.rohit.quizzon.ui.adapter.CreateQuestionAdapter
import com.rohit.quizzon.ui.viewmodels.CreateQuizViewModel
import com.rohit.quizzon.utils.NetworkResponse
import com.rohit.quizzon.utils.autoCleaned
import com.rohit.quizzon.utils.shortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CreateQuizFragment : Fragment() {

    private var binding: FragmentCreateQuizBinding by autoCleaned()
    private val createQuestionAdapter: CreateQuestionAdapter by autoCleaned { CreateQuestionAdapter() }
    private val createQuizViewModel: CreateQuizViewModel by viewModels()
    private val questionList = arrayListOf<CreateQuestionData>()
    private var currentIndex = 0
    private lateinit var quizBody: QuizBody

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateQuizBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        binding.rvQuestionList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = createQuestionAdapter
            isNestedScrollingEnabled = false
        }
        binding.btnAddQuestion.setOnClickListener {
            showDialog()
        }
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun showDialog() {

        val mView = QuestionDialogBinding.inflate(LayoutInflater.from(requireContext()))
        val optionList = listOf(
            "Option 1",
            "Option 2",
            "Option 3",
            "Option 4"
        )
        val arrayAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.test_list_item, optionList
        )

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        (mView.answerId.editText as? AutoCompleteTextView)?.setAdapter(arrayAdapter)

        val dialog = AlertDialog.Builder(requireContext()).create()
        dialog.setView(mView.root)
        dialog.setTitle("Add Questions")
        dialog.show()

        mView.textAdd.setOnClickListener {
            when {
                mView.edQuetionStatement.editText?.text.toString().isEmpty() -> {
                    mView.edQuetionStatement.error = "Enter Quertion Statement"
                }
                mView.edOption1.editText?.text.toString().isEmpty() -> {
                    mView.edOption1.error = "Enter Option 1"
                }
                mView.edOption2.editText?.text.toString().isEmpty() -> {
                    mView.edOption2.error = "Enter Option 1"
                }
                mView.edOption3.editText?.text.toString().isEmpty() -> {
                    mView.edOption3.error = "Enter Option 1"
                }
                mView.edOption4.editText?.text.toString().isEmpty() -> {
                    mView.edOption4.error = "Enter Option 1"
                }
                else -> {
                    saveDataToList(
                        mView.edQuetionStatement.editText!!.text.toString(),
                        mView.edOption1.editText!!.text.toString(),
                        mView.edOption2.editText!!.text.toString(),
                        mView.edOption3.editText!!.text.toString(),
                        mView.edOption4.editText!!.text.toString(),
                        mView.answerId.editText!!.text.toString()
                    )
                    dialog.dismiss()
                }
            }
        }
        mView.textCancel.setOnClickListener {
            dialog.cancel()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.create_quize_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (createQuestionAdapter.itemCount < 5) {
            shortToast("Please Add 5 Question at least")
            false
        } else {
            val quizBody = QuizBody(
                quizeTitle = binding.inputQuizTitle.editText!!.text.toString(),
                questionlist = createQuestionAdapter.currentList,
                category_id = "test",
                category_name = "General",
                create_id = "rrr",
                create_name = "rohit"
            )
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                createQuizViewModel.uploadQuiz(quizBody)
                createQuizViewModel.uploadResponse.collectLatest {
                    when (it) {
                        is NetworkResponse.Success -> shortToast("${it.message} \nid: ${it.data?.insertedHashes}")
                        is NetworkResponse.Failure -> shortToast("error: ${it.message}")
                        is NetworkResponse.Loading -> {
                        }
                    }
                }
            }
            true
        }
    }

    private fun saveDataToList(
        questionStatement: String,
        op1: String,
        op2: String,
        op3: String,
        op4: String,
        answer: String
    ) {
        val createQuestionData = CreateQuestionData(
            questionStatement = questionStatement,
            option1 = op1,
            option2 = op2,
            option3 = op3,
            option4 = op4,
            questionIndex = currentIndex
        )

        questionList.add(createQuestionData)
        createQuestionAdapter.submitList(questionList)
        createQuestionAdapter.notifyDataSetChanged()
        currentIndex += 1

        Log.d(
            "question",
            "que: $questionStatement\noption1: $op1\nption2: $op2\noption3: $op3\noption4: $op4\nanswer: $answer\nIndex: $currentIndex"
        )
    }
}
