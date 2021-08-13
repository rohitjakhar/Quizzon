package com.rohit.quizzon.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.play.core.review.ReviewManagerFactory
import com.rohit.quizzon.databinding.FragmentAboutBinding
import com.rohit.quizzon.utils.autoCleaned
import com.rohit.quizzon.utils.shortToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutFragment : Fragment() {

    private var binding: FragmentAboutBinding by autoCleaned()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        handleClickListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initReview()
    }

    private fun initReview() {
        val manager = ReviewManagerFactory.create(requireContext())
        val request = manager.requestReviewFlow()
        request.addOnSuccessListener { reviewInfo ->
            val flow = manager.launchReviewFlow(requireActivity(), reviewInfo)
            flow.addOnSuccessListener {
            }
        }
    }

    private fun handleClickListener() = binding.apply {
        imgGithub.setOnClickListener {
            openInBrowser(githubLink)
        }
        imgHashnode.setOnClickListener {
            openInBrowser(hashnodeLink)
        }
        imgLinkedIn.setOnClickListener {
            openInBrowser(linkedinLink)
        }
    }

    private fun openInBrowser(link: String) {
        requireActivity().startActivity(
            Intent(Intent.ACTION_VIEW).also {
                it.data = Uri.parse(link)
            }
        )
    }

    companion object {
        const val githubLink = "https://github.com/rohitjakhar/"
        const val linkedinLink = "https://www.linkedin.com/in/rohitjakhar0/"
        const val hashnodeLink = "https://rohitjakhar.hashnode.dev/"
    }
}
