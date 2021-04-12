package com.example.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

private const val ANSWER = "com.example.geoquiz.answer"
const val CHEATED = "com.example.geoquiz.cheated"
private const val TAG = "CheatActivity"
private const val DID_CHEAT = "CHEATED"
private const val ANSWER_TEXT = "ANSWER"

class CheatActivity : AppCompatActivity() {

    private lateinit var answerView: TextView
    private lateinit var showAnswerButton: Button

    private var answer = false
    private var answerText = "False"
    private var cheated = false


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState.putBoolean(DID_CHEAT, cheated)
        outState.putString(ANSWER_TEXT, answerText)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)

        val didCheat = savedInstanceState?.getBoolean(DID_CHEAT, false) ?: false
        cheated = didCheat
        if (cheated) {
            setAnswerShownResult()
        }
        val savedAnswer = savedInstanceState?.getString(ANSWER_TEXT, "") ?: ""
        answerView.text = savedAnswer

        answer = intent.getBooleanExtra(ANSWER, false)
        showAnswerButton.setOnClickListener {
            answerText = when {
                answer -> getString(R.string.true_button)
                else -> getString(R.string.false_button)
            }
            answerView.text = answerText
            cheated = true
            setAnswerShownResult()
        }
    }

    private fun setAnswerShownResult() {
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