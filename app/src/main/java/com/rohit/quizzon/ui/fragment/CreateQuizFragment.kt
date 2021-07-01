package com.rohit.quizzon.ui.fragment

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rohit.quizzon.data.model.CreateQuestionData
import com.rohit.quizzon.data.model.body.QuestionBody
import com.rohit.quizzon.data.model.response.CategoryResponseItem
import com.rohit.quizzon.databinding.DialogQuestionAddBinding
import com.rohit.quizzon.databinding.FragmentCreateQuizBinding
import com.rohit.quizzon.ui.adapter.QuestionAdapter
import com.rohit.quizzon.ui.viewmodels.CreateQuizViewModel
import com.rohit.quizzon.utils.NetworkResponse
import com.rohit.quizzon.utils.action
import com.rohit.quizzon.utils.autoCleaned
import com.rohit.quizzon.utils.listener.CreateQuizListener
import com.rohit.quizzon.utils.shortToast
import com.rohit.quizzon.utils.snack
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CreateQuizFragment : Fragment(), CreateQuizListener {

    private var binding: FragmentCreateQuizBinding by autoCleaned()
    private val createQuestionAdapter: QuestionAdapter by autoCleaned {
        QuestionAdapter(
            this
        )
    }
    private val createQuizViewModel: CreateQuizViewModel by viewModels()
    private val questionList = arrayListOf<CreateQuestionData>()
    private var currentIndex = 0
    private var categoryList = arrayListOf<CategoryResponseItem>()
    private lateinit var selectedCategory: CategoryResponseItem
    private var userName: String = ""
    private var userId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateQuizBinding.inflate(inflater, container, false)
        addViewForProgress()
        initCategory()
        createQuizViewModel.getUserProfile()
        initUser()
        initRecyclerView()
        binding.fabAddQuestion.setOnClickListener {
            showDialog()
        }
        initUploadClickListener()
        return binding.root
    }

    private fun addViewForProgress() = binding.apply {
        btnSaveQuiz.setDisableViews(
            listOf(
                inputQuizTitle,
                categoryQuiz,
                fabAddQuestion,
                rvQuestionList
            )
        )
    }

    private fun initCategory() {
        lifecycleScope.launchWhenStarted {
            createQuizViewModel.getCategoryList()
            createQuizViewModel.categoryList.collectLatest {
                when (it) {
                    is NetworkResponse.Success -> it.data?.let { it1 -> categoryList.addAll(it1) }
                    is NetworkResponse.Failure -> {
                        shortToast("Cant Load Please Refresh")
                        findNavController().navigateUp()
                    }
                    is NetworkResponse.Loading -> {
                    }
                }
            }
        }
        val categoryAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_expandable_list_item_1, categoryList
        )

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        (binding.categoryQuiz.editText as? AutoCompleteTextView)?.setAdapter(categoryAdapter)
        (binding.categoryQuiz.editText as? AutoCompleteTextView)?.setOnItemClickListener { parent, view, position, id ->
            val tet = parent.getItemAtPosition(position) as CategoryResponseItem
            selectedCategory = tet
        }
    }

    private fun initUser() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            createQuizViewModel.userProfile.collectLatest { user ->
                userName = user.username
                userId = user.user_id
            }
        }
    }

    private fun initRecyclerView() = binding.apply {
        rvQuestionList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = createQuestionAdapter
            isNestedScrollingEnabled = false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun showDialog() {
        val mView = DialogQuestionAddBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(requireContext()).create()
        dialog.setView(mView.root)
        dialog.setTitle("Add Questions")
        dialog.show()

        mView.textAdd.setOnClickListener {
            val questionStatement = mView.edQuetionStatement.editText?.text.toString().trim()
            val option1 = mView.edOption1.editText?.text.toString().trim()
            val option2 = mView.edOption2.editText?.text.toString().trim()
            val option3 = mView.edOption3.editText?.text.toString().trim()
            val option4 = mView.edOption4.editText?.text.toString().trim()
            val answer = mView.answerId.editText?.text.toString().trim()

            when {
                questionStatement.isEmpty() -> {
                    mView.edQuetionStatement.error = "Enter Question Statement"
                }
                option1.isEmpty() -> {
                    mView.edOption1.error = "Enter Option 1"
                }
                option2.isEmpty() -> {
                    mView.edOption2.error = "Enter Option 2"
                }
                option3.isEmpty() -> {
                    mView.edOption3.error = "Enter Option 3"
                }
                option4.isEmpty() -> {
                    mView.edOption4.error = "Enter Option 4"
                }
                answer.isEmpty() -> {
                    mView.answerId.error = "Write Answer"
                }
                !checkOption(option1, option2, option3, option4, answer) -> {
                    mView.answerId.error = "Answer Not Match with options"
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

    private fun checkOption(
        option1: String,
        option2: String,
        option3: String,
        option4: String,
        answer: String
    ): Boolean {
        return when {
            answer.equals(option1, false) -> true
            answer.equals(option2, false) -> true
            answer.equals(option3, false) -> true
            answer.equals(option4, false) -> true
            else -> false
        }
    }

    private fun validateInputs(): Boolean {
        return when {
            createQuestionAdapter.itemCount < 1 -> {
                shortToast("Please Add 5 Question at least")
                false
            }
            binding.inputQuizTitle.editText?.text.isNullOrEmpty() -> {
                binding.inputQuizTitle.editText?.error = "Enter Quiz Title"
                return false
            }
            !this::selectedCategory.isInitialized -> {
                binding.categoryQuiz.editText?.error = "Select A Category First"
                return false
            }
            else -> true
        }
    }

    private fun initUploadClickListener() = binding.apply {
        btnSaveQuiz.setOnClickListener {
            if (validateInputs()) {
                uploadQuiz()
                btnSaveQuiz.activate()
            }
        }
    }

    private fun uploadQuiz() {
        val questionBody = QuestionBody(
            quiz_title = binding.inputQuizTitle.editText!!.text.toString(),
            question_list = createQuestionAdapter.currentList,
            category_id = selectedCategory.id,
            category_name = selectedCategory.categoryName,
            create_id = userId,
            create_name = userName,
            total_question = createQuestionAdapter.itemCount
        )
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            createQuizViewModel.uploadQuiz(questionBody)
            createQuizViewModel.uploadResponse.collectLatest { networkResponse ->
                when (networkResponse) {
                    is NetworkResponse.Success -> {
                        binding.btnSaveQuiz.finished()
                        binding.root.snack("Quiz Created! Copy quiz id") {
                            action("Copy") {
                                copyQuizId(networkResponse.data?.insertedHashes?.get(0))
                            }
                        }
                        findNavController().navigateUp()
                    }
                    is NetworkResponse.Failure -> {
                        binding.root.snack("${networkResponse.message}") {
                        }
                        binding.btnSaveQuiz.reset()
                    }
                    is NetworkResponse.Loading -> {
                        binding.btnSaveQuiz.activate()
                    }
                }
            }
        }
    }

    private fun copyQuizId(quizId: String?) {
        if (!quizId.isNullOrEmpty()) {
            val clipboardManager =
                getSystemService(requireContext(), ClipboardManager::class.java) as ClipboardManager
            val clipData = ClipData.newPlainText("text", quizId)
            clipboardManager.setPrimaryClip(clipData)
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
            questionIndex = currentIndex,
            answer = answer
        )
        questionList.add(createQuestionData)
        createQuestionAdapter.submitList(questionList)
        createQuestionAdapter.notifyDataSetChanged()
        currentIndex += 1
    }

    override fun clickDeleteItem(position: Int) {
        questionList.removeAt(position)
        createQuestionAdapter.notifyDataSetChanged()
    }
}
