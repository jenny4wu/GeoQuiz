package com.example.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

private const val ANSWER = "com.example.geoquiz.answer"
const val CHEATED = "com.example.geoquiz.cheated"
const val TIMES_CHEATED = "com.example.geoquiz.timescheated"
private const val TAG = "CheatActivity"
private const val DID_CHEAT = "CHEATED"
private const val ANSWER_TEXT = "ANSWER"
private const val NUM_CHEATS = "NUMCHEATS"

class CheatActivity : AppCompatActivity() {

    private lateinit var answerView: TextView
    private lateinit var showAnswerButton: Button
    private lateinit var apiView: TextView
    private lateinit var numCheatsView: TextView

    private var answer = false
    private var answerText = "False"
    private var cheated = false
    private var timesCheated = 0


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState.putBoolean(DID_CHEAT, cheated)
        outState.putString(ANSWER_TEXT, answerText)
        outState.putInt(NUM_CHEATS, timesCheated)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        apiView = findViewById(R.id.api_level)
        numCheatsView = findViewById(R.id.num_cheats)

        var currApi = "API Level " + Build.VERSION.SDK_INT
        apiView.text = currApi

        val didCheat = savedInstanceState?.getBoolean(DID_CHEAT, false) ?: false
        cheated = didCheat
        if (cheated) {
            setAnswerShownResult()
        }
        val savedAnswer = savedInstanceState?.getString(ANSWER_TEXT, "") ?: ""
        answerView.text = savedAnswer

        timesCheated = intent.getIntExtra(TIMES_CHEATED, 0)
        val numCheats = savedInstanceState?.getInt(NUM_CHEATS, 0) ?: 0
        if (numCheats != 0) {
            timesCheated = numCheats
        }
        updateCheatsLeft()

        answer = intent.getBooleanExtra(ANSWER, false)
        showAnswerButton.setOnClickListener {
            answerText = when {
                answer -> getString(R.string.true_button)
                else -> getString(R.string.false_button)
            }
            answerView.text = answerText
            cheated = true
            timesCheated++
            updateCheatsLeft()
            setAnswerShownResult()
        }
    }

    private fun setAnswerShownResult() {
        val data = Intent().apply {
            putExtra(CHEATED, cheated)
            putExtra(TIMES_CHEATED, timesCheated)
        }
        setResult(Activity.RESULT_OK, data)
    }

    private fun updateCheatsLeft() {
        val cheatText = "Times cheated: $timesCheated/3"
        numCheatsView.text = cheatText
    }

    companion object {
        fun newIntent(packageContext: Context, answer: Boolean, timesCheated: Int): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(ANSWER, answer)
                putExtra(TIMES_CHEATED, timesCheated)
            }
        }
    }
}