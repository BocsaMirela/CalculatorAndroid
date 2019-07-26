package com.example.calculator.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.calculator.business.manager.ICalculatorManager
import io.reactivex.rxkotlin.subscribeBy

class ScientificViewModel(private val manager: ICalculatorManager) : KeyboardViewModel(manager) {

    init {
        progress.postValue(true)
        manager.getScientific().mergeWith(manager.getNumbers()).subscribeBy(onNext = { inputs ->
            mapInput(inputs.sortedBy { it.order })
            progress.postValue(false)
        }, onError = {
            mapInput(manager.getDummyScientific().plus(manager.getDummyNumbers()).sortedBy { it.order })
            progress.postValue(false)
        })
    }

}

class ScientificViewModelFactory(private val manager: ICalculatorManager) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScientificViewModel::class.java)) {
            return ScientificViewModel(manager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}