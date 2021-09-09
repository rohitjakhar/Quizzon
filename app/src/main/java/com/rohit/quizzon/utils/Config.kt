package com.rohit.quizzon.utils

import com.rohit.quizzon.BuildConfig

class Config {
    companion object {
        const val BASE_URL = BuildConfig.BASE_URL
        var currentLanguage = "en"
    }
}
