package com.rohit.quizzon.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

fun Context.shortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.shortToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun MaterialAlertDialogBuilder.showInput(
    layout: Int,
    tilId: Int,
    hintRes: Int,
    counterMaxLength: Int = 0,
    prefilled: String = ""
): Dialog {
    this.setView(layout)
    val dialog = this.show()
    val til = dialog.findViewById<TextInputLayout>(tilId)
    til?.let {
        til.hint = context.getString(hintRes)
        if (counterMaxLength > 0) {
            til.counterMaxLength = counterMaxLength
            til.isCounterEnabled = true
        }
        til.editText?.doOnTextChanged { text, start, before, count ->
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .isEnabled =
                !text.isNullOrBlank() && (counterMaxLength == 0 || text.length <= counterMaxLength)
        }
        til.editText?.append(prefilled)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .isEnabled = prefilled.isNotBlank()
    }
    return dialog
}

fun DialogInterface.inputText(tilId: Int): String {
    return (this as AlertDialog).findViewById<TextInputLayout>(tilId)?.editText?.text?.toString()
        .orEmpty()
}

inline fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}

fun Int.divideToPercent(divideTo: Int): Int {
    return if (divideTo == 0) 0
    else (this.toDouble() / divideTo).toInt() * 100
}
