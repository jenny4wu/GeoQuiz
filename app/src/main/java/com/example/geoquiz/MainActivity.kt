package com.example.geoquiz

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {

    private val quizViewModel : QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var cheatButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var questionNum: TextView
    private lateinit var questionView: TextView
    private lateinit var scoreView: TextView

    private var questionTextResId: Int = 0
    private var score: Int = 0

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState.putInt(KEY_INDEX, quizViewModel.currIndex)
    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currIndex = currIndex

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        cheatButton = findViewById(R.id.cheat_button)
        nextButton = findViewById(R.id.next_button)
        backButton = findViewById(R.id.back_button)
        questionNum = findViewById(R.id.question_number)
        questionView = findViewById(R.id.question_text_view)
        scoreView = findViewById(R.id.score)

        updateQuestion()

        questionView.setOnClickListener { view: View ->
            updateQuestion()
        }
        trueButton.setOnClickListener { view: View ->
            if (!quizViewModel.currAnswered) {
                checkAnswer(true, quizViewModel.currAnswer)
                quizViewModel.updateAnswered(true)
            } else {
                Toast.makeText(this, R.string.answered, Toast.LENGTH_SHORT).show()
            }
        }
        falseButton.setOnClickListener { view: View ->
            if (!quizViewModel.currAnswered) {
                checkAnswer(false, quizViewModel.currAnswer)
                quizViewModel.updateAnswered(true)
            } else {
                Toast.makeText(this, R.string.answered, Toast.LENGTH_SHORT).show()
            }
        }
        cheatButton.isClickable = true
        cheatButton.setOnClickListener { view: View ->
            if (quizViewModel.timesCheated >= 3) {
                Toast.makeText(this, "You've run out of cheats!", Toast.LENGTH_SHORT).show()
            } else {
                val answer = quizViewModel.currAnswer
                val intent =
                    CheatActivity.newIntent(this@MainActivity, answer, quizViewModel.timesCheated)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val options =
                        ActivityOptions.makeClipRevealAnimation(view, 0, 0, view.width, view.height)
                    startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle())
                } else {
                    startActivityForResult(intent, REQUEST_CODE_CHEAT)
                }
            }
        }
        nextButton.setOnClickListener { view: View ->
            quizViewModel.nextQuestion()
            updateQuestion()
        }
        backButton.setOnClickListener { view: View ->
            quizViewModel.prevQuestion()
            updateQuestion()
        }
    }

    private fun updateQuestion() {
        questionTextResId = quizViewModel.currQuestion
        questionNum.setText(String.format(getString(R.string.num_questions), quizViewModel.currIndex+1))
        questionView.setText(questionTextResId)
    }

    private fun checkAnswer(clicked: Boolean, answer: Boolean) {
        var toastText = if (clicked == answer) {
            score++
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        val finalToastMessage = when {
            quizViewModel.cheated -> R.string.judgement_toast
            else -> toastText
        }
        Toast.makeText(this, finalToastMessage, Toast.LENGTH_SHORT).show()
        scoreView.setText(String.format(getString(R.string.score), score, quizViewModel.getNumQuestions()))
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.cheated = data?.getBooleanExtra(CHEATED, false) ?: false
            quizViewModel.timesCheated = data?.getIntExtra(TIMES_CHEATED, 0) ?: 0
        }
    }
}