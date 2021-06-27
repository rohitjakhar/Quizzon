package com.rohit.quizzon.utils.listener

import com.rohit.quizzon.data.model.response.QuizResponse

interface QuizClickListener {
    fun quizClickListener(quizListBody: QuizResponse)
}
