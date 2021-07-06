package com.example.calculationtest

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle

class MyViewModel(application: Application, savedStateHandle: SavedStateHandle) :
    AndroidViewModel(application) {
    private var handle: SavedStateHandle
    var winFlag = false

    init {
        if (!savedStateHandle.contains("key_high_score")) {
            val sharedPreferences =
                application.getSharedPreferences("save_shp_data_name", Context.MODE_PRIVATE)
            with(savedStateHandle) {
                set("key_high_score", sharedPreferences.getInt("key_high_score", 0))
                set("key_left_number", 0)
                set("key_right_number", 0)
                set("key_answer", 0)
                set("key_operator", "+")
                set("key_current_score", 0)
            }
        }
        handle = savedStateHandle
    }

    val highScore: MutableLiveData<Int> = handle.getLiveData("key_high_score")
    val leftNumber: MutableLiveData<Int> = handle.getLiveData("key_left_number")
    val rightNumber: MutableLiveData<Int> = handle.getLiveData("key_right_number")
    val answer: MutableLiveData<Int> = handle.getLiveData("key_answer")
    val operator: MutableLiveData<String> = handle.getLiveData("key_operator")
    val currentScore: MutableLiveData<Int> = handle.getLiveData("key_current_score")

    fun generator() {
        val x = (0..20).random()
        val y = (0..20).random()
        if (x % 2 == 0) {
            operator.value = "+"
            //加法大数作answer
            if (x > y) {
                leftNumber.value = y
                rightNumber.value = x - y
                answer.value = x
            } else {
                leftNumber.value = x
                rightNumber.value = y - x
                answer.value = y
            }
        } else {
            operator.value = "-"
            //减法大数作left
            if (x > y) {
                leftNumber.value = x
                rightNumber.value = y
                answer.value = x - y
            } else {
                leftNumber.value = y
                rightNumber.value = x
                answer.value = y - x
            }
        }
    }

    fun answerCorrect() {
        currentScore.value = currentScore.value?.plus(1)
        if (currentScore.value!! > highScore.value!!) {
            highScore.value = currentScore.value
            winFlag = true
        }
        generator()
    }

    fun save() {
        getApplication<Application>().getSharedPreferences(
            "save_shp_data_name",
            Context.MODE_PRIVATE
        ).edit().apply {
            highScore.value?.let { putInt("key_high_score", it) }
            apply()
        }
    }
}