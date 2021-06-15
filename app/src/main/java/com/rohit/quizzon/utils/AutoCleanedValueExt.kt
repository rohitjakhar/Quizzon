package com.rohit.quizzon.utils

import androidx.fragment.app.Fragment

fun <T : Any> Fragment.autoCleaned(initializer: (() -> T)? = null): AutoCleanedValue<T> {
    return AutoCleanedValue(this, initializer)
}
