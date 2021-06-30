package com.rohit.quizzon.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rohit.quizzon.databinding.FragmentAboutBinding
import com.rohit.quizzon.utils.autoCleaned
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
