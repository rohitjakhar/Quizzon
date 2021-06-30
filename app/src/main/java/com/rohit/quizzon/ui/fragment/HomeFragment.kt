package com.rohit.quizzon.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rohit.quizzon.databinding.DialogQuizIdBinding
import com.rohit.quizzon.databinding.FragmentHomeBinding
import com.rohit.quizzon.ui.adapter.CategoryAdapter
import com.rohit.quizzon.ui.viewmodels.HomeViewModel
import com.rohit.quizzon.utils.NetworkResponse
import com.rohit.quizzon.utils.autoCleaned
import com.rohit.quizzon.utils.listener.CategoryClickListner
import com.rohit.quizzon.utils.shortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : Fragment(), CategoryClickListner {

    private var binding: FragmentHomeBinding by autoCleaned()
    private var categoryAdapter by autoCleaned { CategoryAdapter(this) }
    private val homeViewModel: HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        startShimmer()
        binding.rvCategory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = categoryAdapter
        }
        homeViewModel.getCategoryList()
        loadData()
        binding.cvCreateAQuiz.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavCreateQuiz())
        }
        binding.cvJoinQuiz.setOnClickListener {
            showInputDialog()
        }
        return binding.root
    }

    private fun showInputDialog() {
        val mView = DialogQuizIdBinding.inflate(layoutInflater)
        mView.btnJoinQuiz.setDisableViews(listOf(mView.textInputLayout))
        val dialog = MaterialAlertDialogBuilder(requireContext()).create()
        dialog.setView(mView.root)
        dialog.setTitle("Join Quiz")
        dialog.show()

        mView.btnJoinQuiz.setOnClickListener {
            val quizid = mView.textInputLayout.editText?.text?.trim().toString()
            if (quizid.isNotEmpty()) {
                mView.btnJoinQuiz.activate()
                homeViewModel.loadQuiz(quizid)
                loadQuizDataAndNavigateQuizPlay(mView)
            } else {
                mView.textInputLayout.error = "Enter Correct Quiz Id"
            }
        }
    }

    private fun loadQuizDataAndNavigateQuizPlay(mView: DialogQuizIdBinding) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            homeViewModel.quizData.collectLatest { value ->
                when (value) {
                    is NetworkResponse.Success -> {
                        mView.btnJoinQuiz.finished()
                        value.data?.let {
                            findNavController().navigate(
                                HomeFragmentDirections.actionNavHomeToQuizFragment(
                                    it
                                )
                            )
                        } ?: shortToast("Not Found Data")
                    }
                    is NetworkResponse.Failure -> {
                        mView.btnJoinQuiz.reset()
                        shortToast("${value.message}")
                    }
                    is NetworkResponse.Loading -> {
                        mView.btnJoinQuiz.activate()
                    }
                }
            }
        }
    }

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            homeViewModel.categoryList.collectLatest { networkResponse ->
                when (networkResponse) {
                    is NetworkResponse.Success -> {
                        val categoryList = networkResponse.data?.sortedBy {
                            it.categoryName
                        }
                        categoryAdapter.submitList(categoryList)
                        stopShimmer()
                    }
                    is NetworkResponse.Failure -> {
                        stopShimmer()
                        networkResponse.message?.let { it1 -> shortToast(it1) }
                    }
                    is NetworkResponse.Loading -> {
                        startShimmer()
                    }
                }
            }
        }
    }

    override fun onCategoryListner(category_id: String) {
        Toast.makeText(requireContext(), "Clicked on $category_id", Toast.LENGTH_LONG).show()
        val ac = HomeFragmentDirections.actionNavHomeToQuizListFragment(category_id)
        findNavController().navigate(ac)
    }

    private fun startShimmer() {
        binding.apply {
            homeShimmerEffect.visibility = View.VISIBLE
            homeShimmerEffect.startShimmer()
            rvCategory.visibility = View.GONE
        }
    }

    private fun stopShimmer() {
        binding.apply {
            homeShimmerEffect.visibility = View.GONE
            homeShimmerEffect.stopShimmer()
            rvCategory.visibility = View.VISIBLE
        }
    }
}
