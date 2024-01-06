package com.gontory.elqirasygontory.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gontory.elqirasygontory.data.Mutholaah
import com.gontory.elqirasygontory.data.Result
import com.gontory.elqirasygontory.firebase.FirebaseCommon
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResultViewModel : ViewModel() {

    private val firebaseFirestore = FirebaseCommon()

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _scoreProgress = MutableLiveData<Int>()
    val scoreProgress: LiveData<Int> get() = _scoreProgress

    private val _result = MutableLiveData<Boolean>()
    val result: LiveData<Boolean> get() = _result

    private val _correctScore = MutableLiveData<Int>()
    val correctScore: LiveData<Int> get() = _correctScore

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun fetchQuizResult(quizListModel: Mutholaah) {
        uiScope.launch {
            _isLoading.value = true
            getResult(quizListModel)
            _isLoading.value = false
        }
    }

    private suspend fun getResult(quizListModel: Mutholaah) {
        withContext(Dispatchers.IO) {

            val value = firebaseFirestore.getResultsById(quizListModel.mutholaahId)

            val result = value?.toObject<Result>()

            if (result != null) {
                val correct = result.correct
                val wrong = result.wrong
                val missed = result.unanswered

                _correctScore.postValue(correct.toInt())
                val total = correct + wrong + missed

                val percent = (correct * 100) / total
                _scoreProgress.postValue(percent.toInt())

                val passed = correct > (total / 2)
                _result.postValue(passed)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}