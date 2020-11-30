package com.example.geoquiz

import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {

    private val questionBank = listOf(
        Question(R.string.question_africa, answer = false, answered = false),
        Question(R.string.question_america, answer = true, answered = false),
        Question(R.string.question_asia, answer = true, answered = false),
        Question(R.string.question_australia, answer = true, answered = false),
        Question(R.string.question_mideast, answer = false, answered = false),
        Question(R.string.question_oceans, answer = true, answered = false)
    )

    var currIndex = 0

    fun nextQuestion() {
        currIndex = (currIndex  + 1) % questionBank.size
    }

    fun prevQuestion() {
        currIndex = if (currIndex == 0) {
            questionBank.size - 1
        } else {
            (currIndex - 1) % questionBank.size
        }
    }

    fun getNumQuestions() : Int {
        return questionBank.size
    }

    fun updateAnswered(answered: Boolean) {
        questionBank[currIndex].answered = answered
    }

    val currQuestion: Int get() = questionBank[currIndex].textResId
    val currAnswer: Boolean get() = questionBank[currIndex].answer
    val currAnswered: Boolean get() = questionBank[currIndex].answered



}