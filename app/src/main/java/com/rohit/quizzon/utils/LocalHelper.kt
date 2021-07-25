package com.rohit.quizzon.utils

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import androidx.core.graphics.TypefaceCompatUtil
import java.util.*

object LocalHelper {
    fun setAppLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.createConfigurationContext(config)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}
