package com.rohit.quizzon.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.rohit.quizzon.data.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    remoteRepository: RemoteRepository
) : ViewModel()
