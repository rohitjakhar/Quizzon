package com.rohit.quizzon.ui.viewholder

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rohit.quizzon.R
import com.rohit.quizzon.data.model.response.QuizResponse
import com.rohit.quizzon.databinding.SingleUserQuizBinding
import com.rohit.quizzon.utils.listener.QuizClickListener
import com.rohit.quizzon.utils.snack

class UserQuizViewHolder(
    private val binding: SingleUserQuizBinding,
    private val quizClickListener: QuizClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(quizResponse: QuizResponse) = binding.apply {
        val context = binding.root.context
        textQuestionIndex.text = absoluteAdapterPosition.plus(1).toString()
        textQuizTitle.text = quizResponse.quizTitle
        imgDeleteItem.setOnClickListener {
            MaterialAlertDialogBuilder(binding.root.context)
                .setTitle("Delete Quiz")
                .setPositiveButton(context.resources.getString(R.string.delete_quiz)) { dialog, _ ->
                    quizClickListener.quizClickListener(quizResponse)
                    dialog.dismiss()
                }
                .setNeutralButton(context.resources.getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
        imgCopyLink.setOnClickListener {
            val clipboardManager =
                ContextCompat.getSystemService(
                    context,
                    ClipboardManager::class.java
                ) as ClipboardManager
            val clipData = ClipData.newPlainText("text", quizResponse.quizId)
            clipboardManager.setPrimaryClip(clipData)
            binding.root.snack(context.resources.getString(R.string.copied_link)) {}
        }
        imgShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Play My Quiz '${quizResponse.quizTitle}' related to ${quizResponse.categoryName} category in Quizzon app created by ${quizResponse.createName}.\nQuiz id is: ${quizResponse.quizId}"
                )
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        }
    }

    companion object {
        fun create(parent: ViewGroup, quizClickListener: QuizClickListener): UserQuizViewHolder {
            val mView = LayoutInflater.from(parent.context)
            val binding = SingleUserQuizBinding.inflate(mView, parent, false)
            return UserQuizViewHolder(binding, quizClickListener)
        }
    }
}
