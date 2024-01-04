package com.gontory.elqirasygontory.ui.quiz

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gontory.elqirasygontory.data.Mutholaah
import com.gontory.elqirasygontory.data.Question
import com.gontory.elqirasygontory.firebase.FirebaseCommon
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "QuizViewModel"

class QuizViewModel: ViewModel() {

    private val firebaseFirestore = FirebaseCommon()

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    lateinit var quizMutholaah: Mutholaah
    private var totalQuestionToAnswer: Long = 0

    private var allQuestionList = mutableListOf<Question>()
    private val questionToAnswer = mutableListOf<Question>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _quizTitle = MutableLiveData<String>()
    val quizTitle: LiveData<String> get() = _quizTitle

    private val _questionNumber = MutableLiveData<Int>()
    val questionNumber: LiveData<Int> get() = _questionNumber

    private val _questionsTotalNumber = MutableLiveData<Long>()
    val questionTotalNumber: LiveData<Long> get() = _questionsTotalNumber

    private val _questionTime = MutableLiveData<String>()
    val questionTime: LiveData<String> get() = _questionTime

    private val _questionProgress = MutableLiveData<Int>()
    val questionProgress: LiveData<Int> get() = _questionProgress

    private val _questionText = MutableLiveData<String>()
    val questionText: LiveData<String> get() = _questionText

    // Options
    private val _optionA = MutableLiveData<String>()
    val optionA: LiveData<String> get() = _optionA

    private val _optionB = MutableLiveData<String>()
    val optionB: LiveData<String> get() = _optionB

    private val _optionC = MutableLiveData<String>()
    val optionC: LiveData<String> get() = _optionC

    private val _optionD = MutableLiveData<String>()
    val optionD: LiveData<String> get() = _optionD

    private val _isTimeUp = MutableLiveData<Boolean>()
    val isTimeUp: LiveData<Boolean> get() = _isTimeUp

    private val _shouldNavigateToResult = MutableLiveData<Boolean>()
    val shouldNavigateToResult: LiveData<Boolean> get() = _shouldNavigateToResult

    // Quetion Time
    private lateinit var timer: CountDownTimer

    // Can Answer
    private var canAnswer: Boolean = false
    private var correctAnswer: Int = 0
    private var wrongAnswer: Int = 0
    private var currentQuestionNumber: Int = 1
    private var notAnswered: Int = 0

    fun initializeQuetions(quizListModel: Mutholaah) {

        uiScope.launch {
            isLoading(true)

            quizMutholaah = quizListModel
            totalQuestionToAnswer = quizListModel.questions

            _quizTitle.value = quizListModel.title
            _questionsTotalNumber.value = quizListModel.questions

            fetchQuestions()

            isLoading(false)
        }

    }

    private suspend fun fetchQuestions() {
        withContext(Dispatchers.IO) {
            val value = firebaseFirestore.getQuizQuestions(quizMutholaah.mutholaahId)

            val questionModelList: MutableList<Question> = mutableListOf()
            for (doc in value!!) {
                val questionItem = doc.toObject<Question>()
                questionModelList.add(questionItem)
            }

            allQuestionList = questionModelList
        }

        pickQuestions()
        loadQuestion(currentQuestionNumber)
    }

    private fun loadQuestion(questionNumber: Int) {
        // Question number
        _questionNumber.value = questionNumber

        // Load Question
        _questionText.value = questionToAnswer[questionNumber - 1].question

        shuffleChoices(questionNumber)

        // Question Loaded. set can Answer
        canAnswer = true
        currentQuestionNumber = questionNumber

        // start question timer
        startTimer(questionNumber)
    }

    fun loadNextQuestion() {
        currentQuestionNumber++

        if (currentQuestionNumber > totalQuestionToAnswer) {
            submitResults()
        } else {
            loadQuestion(currentQuestionNumber)
        }
    }

    fun getCorrectAnswer(selectedAnswer: String): String {
        // Check Answer
        if (canAnswer) {
            if (questionToAnswer[currentQuestionNumber - 1].answer == selectedAnswer) {
                // Correct Answer
                correctAnswer++
                Log.d(TAG, "Correct Answer")
            } else {
                wrongAnswer++
                Log.d(TAG, "Wrong Answer")
            }
            // Set can answer to false
            canAnswer = false
            // Stop timer
            timer.cancel()
        }
        return questionToAnswer[currentQuestionNumber - 1].answer
    }

    private fun submitResults() {
        uiScope.launch {
            isLoading(true)

            val resultMap = HashMap<String, Any?>()

            resultMap["correct"] = correctAnswer
            resultMap["wrong"] = wrongAnswer
            resultMap["unanswered"] = notAnswered

            resultMap["title"] = quizMutholaah.title
            resultMap["urutan"] = quizMutholaah.urutan

            submit(resultMap)
        }
    }

    private suspend fun submit(resultMap: HashMap<String, Any?>) {
        withContext(Dispatchers.IO) {
            try {
                firebaseFirestore.submitQuizResult(
                    quizMutholaah.mutholaahId,
                    resultMap
                )
                navigateToResultPage()
            } catch (e: Exception) {
                _quizTitle.postValue(e.message)
            }
        }
    }

    private fun startTimer(questionNumber: Int) {
        // Set timer text
        val timeToAnswer = questionToAnswer[questionNumber - 1].time
        _questionTime.value = timeToAnswer.toString()

        // Start Countdown
        timer = object : CountDownTimer(timeToAnswer + 20000, 20) {

            override fun onFinish() {
                // Time up
                canAnswer = false
                notAnswered++
                onTimeUp()
            }

            override fun onTick(milisUntilFinished: Long) {
                _questionTime.value = (milisUntilFinished / 1000).toString()

                val percent = milisUntilFinished / (timeToAnswer * 10)
                _questionProgress.value = percent.toInt()
            }
        }
        timer.start()
    }

    private fun shuffleChoices(questionNumber: Int) {

        var answers = listOf(
            questionToAnswer[questionNumber - 1].optionA,
            questionToAnswer[questionNumber - 1].optionB,
            questionToAnswer[questionNumber - 1].optionC,
            questionToAnswer[questionNumber - 1].optionD,
        ).toMutableList()

        // Clear choices
        _optionA.value = answers.getOrNull(0) ?: ""
        _optionB.value = answers.getOrNull(0) ?: ""
        _optionC.value = answers.getOrNull(0) ?: ""
        _optionD.value = answers.getOrNull(0) ?: ""

        answers  = answers.filter { it.isNotEmpty() }.toMutableList()

        for (i in 0 until answers.size) {
            val j = (0..i).random()

            val temp: String = answers[i]
            answers[i] = answers[j]
            answers[j] = temp
        }

        mutableListOf(_optionA, _optionB, _optionC, _optionD).forEachIndexed { index, it ->
            if (index <= answers.size - 1) {
                it.value = answers[index]
            }
        }
    }

    private fun pickQuestions() {
        for (i in 0 until totalQuestionToAnswer.toInt()) {
            val randomNumber = getRandomInteger(allQuestionList.size)
            questionToAnswer.add(allQuestionList[randomNumber])
            allQuestionList.removeAt(randomNumber)

            Log.d(TAG, "Question $i" + questionToAnswer[i].question)
        }
    }

    private fun getRandomInteger(maximum: Int, minimum: Int = 0): Int {
        return ((Math.random() * (maximum - minimum))).toInt() + minimum
    }

    private fun onTimeUp() {
        _isTimeUp.value = true
    }

    fun onTimeUpComplete() {
        _isTimeUp.value = false
    }

    private fun navigateToResultPage() {
        _shouldNavigateToResult.postValue(true)
    }

    fun navigateToResultPageComplete() {
        _shouldNavigateToResult.value = false
    }

    fun canAnswer(): Boolean {
        return canAnswer
    }

    private fun isLoading(bool: Boolean) {
        _isLoading.value = bool
    }
}