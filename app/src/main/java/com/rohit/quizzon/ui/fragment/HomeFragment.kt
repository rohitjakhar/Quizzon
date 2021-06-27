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
import com.rohit.quizzon.databinding.FragmentHomeBinding
import com.rohit.quizzon.ui.adapter.CategoryAdapter
import com.rohit.quizzon.ui.viewmodels.HomeViewModel
import com.rohit.quizzon.utils.listener.CategoryClickListner
import com.rohit.quizzon.utils.NetworkResponse
import com.rohit.quizzon.utils.autoCleaned
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
        return binding.root
    }

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            homeViewModel.categoryList.collectLatest {
                when (it) {
                    is NetworkResponse.Success -> {
                        categoryAdapter.submitList(it.data)
                        stopShimmer()
                    }
                    is NetworkResponse.Failure -> {
                        stopShimmer()
                        it.message?.let { it1 -> shortToast(it1) }
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
