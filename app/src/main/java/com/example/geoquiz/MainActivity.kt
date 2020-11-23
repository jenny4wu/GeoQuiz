package com.example.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var questionNum: TextView
    private lateinit var questionView: TextView
    private lateinit var scoreView: TextView

    private var questionTextResId: Int = 0
    private var answered: Boolean = false
    private var score: Int = 0

    private val questionBank = listOf(
        Question(R.string.question_africa, false, false),
        Question(R.string.question_america, true, false),
        Question(R.string.question_asia, true, false),
        Question(R.string.question_australia, true, false),
        Question(R.string.question_mideast, false, false),
        Question(R.string.question_oceans, true, false)
    )

    private var currIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        backButton = findViewById(R.id.back_button)
        questionNum = findViewById(R.id.question_number)
        questionView = findViewById(R.id.question_text_view)
        scoreView = findViewById(R.id.score)

        updateQuestion(0)
        questionView.setOnClickListener { view: View ->
            updateQuestion(1)
        }
        trueButton.setOnClickListener { view: View ->
            if (!questionBank[currIndex].answered) {
                checkAnswer(true, questionBank[currIndex].answer)
                questionBank[currIndex].answered = true
            } else {
                Toast.makeText(this, R.string.answered, Toast.LENGTH_SHORT).show()
            }
        }
        falseButton.setOnClickListener { view: View ->
            if (!questionBank[currIndex].answered) {
                checkAnswer(false, questionBank[currIndex].answer)
                questionBank[currIndex].answered = true
            } else {
                Toast.makeText(this, R.string.answered, Toast.LENGTH_SHORT).show()
            }
        }
        nextButton.setOnClickListener { view: View ->
            updateQuestion(1)
        }
        backButton.setOnClickListener { view: View ->
            updateQuestion(-1)
        }
    }

    private fun updateQuestion(increment: Int) {
        var temp = currIndex + increment
        if (temp >= questionBank.size || temp < 0) {
            Toast.makeText(this, R.string.no_more_questions, Toast.LENGTH_SHORT).show()
        } else {
            answered = false
            currIndex = temp
            questionTextResId = questionBank[currIndex].textResId
            questionNum.setText(String.format(getString(R.string.num_questions), currIndex+1))
            questionView.setText(questionTextResId)
        }
    }

    private fun checkAnswer(clicked: Boolean, answer: Boolean) {
        var toastText = if (clicked == answer) {
            score++
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
        scoreView.setText(String.format(getString(R.string.score), score, questionBank.size))
    }
}