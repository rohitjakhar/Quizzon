package com.rohit.quizzon.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rohit.quizzon.data.model.response.QuizResponse
import com.rohit.quizzon.databinding.FragmentSearchBinding
import com.rohit.quizzon.ui.adapter.QuizAdapter
import com.rohit.quizzon.ui.viewmodels.SearchViewModel
import com.rohit.quizzon.utils.NetworkResponse
import com.rohit.quizzon.utils.autoCleaned
import com.rohit.quizzon.utils.listener.QuizClickListener
import com.rohit.quizzon.utils.shortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SearchFragment : Fragment(), QuizClickListener {
    private var binding: FragmentSearchBinding by autoCleaned()
    private val searchViewModel: SearchViewModel by viewModels()
    private val quizAdapter: QuizAdapter by autoCleaned { QuizAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        startShimmer()
        binding.quizRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = quizAdapter
        }
        searchQuiz()
        searchViewModel.getQuizList()
        loadData()
        return binding.root
    }

    private fun searchQuiz() {
    }

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            searchViewModel.quizList.collectLatest {
                when (it) {
                    is NetworkResponse.Success -> {
                        quizAdapter.submitList(it.data)
                        stopShimmer()
                    }
                    is NetworkResponse.Failure -> {
                        it.message?.let { it1 -> shortToast(it1) }
                        stopShimmer()
                    }
                    is NetworkResponse.Loading -> {
                        startShimmer()
                    }
                }
            }
        }
    }

    override fun quizClickListener(quizListBody: QuizResponse) {
        findNavController().navigate(
            SearchFragmentDirections.actionNavSearchToQuizFragment(
                quizListBody
            )
        )
    }

    private fun startShimmer() {
        binding.apply {
            homeShimmerEffect.visibility = View.VISIBLE
            homeShimmerEffect.startShimmer()
            quizRecyclerView.visibility = View.GONE
        }
    }

    private fun stopShimmer() {
        binding.apply {
            homeShimmerEffect.visibility = View.GONE
            homeShimmerEffect.stopShimmer()
            quizRecyclerView.visibility = View.VISIBLE
        }
    }
}
