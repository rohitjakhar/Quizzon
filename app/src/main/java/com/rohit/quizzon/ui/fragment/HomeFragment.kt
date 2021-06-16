package com.rohit.quizzon.ui.fragment

import android.os.Bundle
import android.util.Log
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
import com.rohit.quizzon.utils.CategoryClickListner
import com.rohit.quizzon.utils.autoCleaned
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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

        loadData()
        binding.rvCategory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = categoryAdapter
        }

        return binding.root
    }

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.fetchCategory()

            Log.d("test121", "called")
            homeViewModel.categoryList.observe(
                viewLifecycleOwner,
                {
                    categoryAdapter.submitData(viewLifecycleOwner.lifecycle, it)

                    Log.d("test121", "sizein fragment: $it")
                }
            )
        }
    }

    override fun onCategoryListner(category_id: String) {
        Toast.makeText(requireContext(), "Clicked on $category_id", Toast.LENGTH_LONG).show()
        val ac = HomeFragmentDirections.actionNavHomeToQuizListFragment(category_id)
        findNavController().navigate(ac)
    }
}
