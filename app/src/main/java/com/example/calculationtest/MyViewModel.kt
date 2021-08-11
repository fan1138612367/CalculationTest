package com.example.calculationtest

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyViewModel(application: Application, private val savedStateHandle: SavedStateHandle) :
    AndroidViewModel(application) {
    private val _highScore: MutableLiveData<Int> by lazy { savedStateHandle.getLiveData("KEY_HIGH_SCORE") }
    val highScore: LiveData<Int> get() = _highScore
    private val _leftNumber: MutableLiveData<Int> by lazy { savedStateHandle.getLiveData("KEY_LEFT_NUMBER") }
    val leftNumber: LiveData<Int> get() = _leftNumber
    private val _rightNumber: MutableLiveData<Int> by lazy { savedStateHandle.getLiveData("KEY_RIGHT_NUMBER") }
    val rightNumber: LiveData<Int> get() = _rightNumber
    private val _answer: MutableLiveData<Int> by lazy { savedStateHandle.getLiveData("KEY_ANSWER") }
    val answer: LiveData<Int> get() = _answer
    private val _operator: MutableLiveData<String> by lazy { savedStateHandle.getLiveData("KEY_OPERATOR") }
    val operator: LiveData<String> get() = _operator
    val currentScore: MutableLiveData<Int> by lazy { savedStateHandle.getLiveData("KEY_CURRENT_SCORE") }
    private val _inputText: MutableLiveData<String> by lazy { savedStateHandle.getLiveData("KEY_INPUT_TEXT") }
    val inputText: LiveData<String> get() = _inputText
    var winFlag = false
    private val sharedPreferences by lazy {
        application.getSharedPreferences("SAVE_SHP_DATA_NAME", Context.MODE_PRIVATE)
    }

    init {
        if (!savedStateHandle.contains("KEY_HIGH_SCORE")) {
            with(savedStateHandle) {
                set("KEY_HIGH_SCORE", sharedPreferences.getInt("KEY_HIGH_SCORE", 0))
                set("KEY_LEFT_NUMBER", 0)
                set("KEY_RIGHT_NUMBER", 0)
                set("KEY_ANSWER", 0)
                set("KEY_OPERATOR", "+")
                set("KEY_CURRENT_SCORE", 0)
                set("KEY_INPUT_TEXT", application.getString(R.string.input_indicator))
            }
        }
    }

    fun generator() {
        val x = (0..20).random()
        val y = (0..20).random()
        if (x % 2 == 0) {
            _operator.value = "+"
            //加法，大数作answer
            if (x > y) {
                _leftNumber.value = y
                _rightNumber.value = x - y
                _answer.value = x
            } else {
                _leftNumber.value = x
                _rightNumber.value = y - x
                _answer.value = y
            }
        } else {
            _operator.value = "-"
            //减法，大数作left
            if (x > y) {
                _leftNumber.value = x
                _rightNumber.value = y
                _answer.value = x - y
            } else {
                _leftNumber.value = y
                _rightNumber.value = x
                _answer.value = y - x
            }
        }
        _inputText.value = getApplication<Application>().getString(R.string.input_indicator)
    }

    fun inputText(charSequence: CharSequence) {
        if (_inputText.value!!.length > 1 || charSequence.length > 1) {
            _inputText.value = charSequence.toString()
        } else {
            _inputText.value += charSequence
        }
    }

    fun answerCorrect() {
        currentScore.value = currentScore.value?.plus(1)
        if (currentScore.value!! > _highScore.value!!) {
            _highScore.value = currentScore.value
            winFlag = true
        }
        generator()
        _inputText.value = getApplication<Application>().getString(R.string.answer_correct_message)
    }

    suspend fun save() {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit {
                putInt("KEY_HIGH_SCORE", highScore.value!!)
            }
        }
    }
}