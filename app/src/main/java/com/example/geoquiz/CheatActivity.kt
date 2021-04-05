package com.example.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

private const val ANSWER = "com.example.geoquiz.answer"
const val CHEATED = "com.example.geoquiz.cheated"

class CheatActivity : AppCompatActivity() {

    private lateinit var answerView: TextView
    private lateinit var showAnswerButton: Button

    private var answer = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answer = intent.getBooleanExtra(ANSWER, false)

        answerView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        showAnswerButton.setOnClickListener {
            val answerText = when {
                answer -> R.string.true_button
                else -> R.string.false_button
            }
            answerView.setText(answerText)
            setAnswerShownResult(true)
        }
    }

    private fun setAnswerShownResult(cheated: Boolean) {
        val data = Intent().apply {
            putExtra(CHEATED, cheated)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answer: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(ANSWER, answer)
            }
        }
    }
}