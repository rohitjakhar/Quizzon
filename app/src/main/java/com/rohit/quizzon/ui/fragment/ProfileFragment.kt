package com.rohit.quizzon.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.rohit.quizzon.data.model.body.UserProfileBody
import com.rohit.quizzon.data.model.response.DeleteResponseBody
import com.rohit.quizzon.data.model.response.QuizResponse
import com.rohit.quizzon.databinding.FragmentProfileBinding
import com.rohit.quizzon.ui.SplashScreen
import com.rohit.quizzon.ui.adapter.UserQuizAdapter
import com.rohit.quizzon.ui.viewmodels.ProfileViewModel
import com.rohit.quizzon.utils.NetworkResponse
import com.rohit.quizzon.utils.autoCleaned
import com.rohit.quizzon.utils.listener.QuizClickListener
import com.rohit.quizzon.utils.snack
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(), QuizClickListener {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    private var binding: FragmentProfileBinding by autoCleaned()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val userQuizAdapter: UserQuizAdapter by autoCleaned { UserQuizAdapter(this) }
    private var userProfileBody = UserProfileBody("", "", "")
    private var quizList = arrayListOf<QuizResponse>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        initRecyclerView()
        profileViewModel.getUserProfile()
        loadUserProfile()
        binding.btnUserLogout.setOnClickListener {
            firebaseAuth.signOut()
            profileViewModel.clearDataStore()
            requireActivity().apply {
                startActivity(Intent(this, SplashScreen::class.java))
                finish()
            }
        }
        return binding.root
    }

    private fun initRecyclerView() = binding.apply {
        rvUserQuizzes.layoutManager = LinearLayoutManager(requireContext())
        rvUserQuizzes.adapter = userQuizAdapter
    }

    private fun loadUserProfile() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            profileViewModel.userProfile.collectLatest {
                userProfileBody = it
                profileViewModel.getUserQuizList(it.user_id)
                loadUserQuizList()
            }
        }
    }

    private fun loadUserQuizList() {
        showShimmerLoading()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            profileViewModel.quizList.collectLatest { value: NetworkResponse<List<QuizResponse>> ->
                when (value) {
                    is NetworkResponse.Success -> {
                        value.data?.let {
                            quizList.clear()
                            quizList.addAll(it)
                            userQuizAdapter.submitList(quizList)
                            userQuizAdapter.notifyDataSetChanged()
                        }
                        stopShimmerLoading()
                    }
                    is NetworkResponse.Failure -> {
                        binding.root.snack("${value.message}") {
                        }
                        stopShimmerLoading()
                    }
                    is NetworkResponse.Loading -> {
                        showShimmerLoading()
                    }
                }
            }
        }
    }

    private fun stopShimmerLoading() = binding.apply {
        homeShimmerEffect.stopShimmer()
        homeShimmerEffect.isVisible = false
        rvUserQuizzes.isVisible = true
        updateView()
    }

    private fun showShimmerLoading() = binding.apply {
        homeShimmerEffect.isVisible = true
        rvUserQuizzes.isVisible = false
        homeShimmerEffect.startShimmer()
    }

    @SuppressLint("SetTextI18n")
    private fun updateView() = binding.apply {
        textUserName.text = userProfileBody.username
        textUserEmail.text = userProfileBody.userEmail
        textUserNumberOfQuiz.text = "Total Quizzes: ${quizList.size}"
        textEmptyData.isVisible = quizList.isEmpty()
    }

    override fun quizClickListener(quizListBody: QuizResponse) {
        profileViewModel.deleteQuiz(quizListBody.quizId)
        quizList.remove(quizListBody)
        userQuizAdapter.notifyDataSetChanged()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            profileViewModel.deleteQuizReponse.collectLatest { value: NetworkResponse<DeleteResponseBody> ->
                when (value) {
                    is NetworkResponse.Success -> {
                        binding.root.snack("Deleted") {}
                    }
                    is NetworkResponse.Failure -> {
                        binding.root.snack("${value.message}") {}
                    }
                    is NetworkResponse.Loading -> {
                    }
                }
            }
        }
    }
}
