package com.example.calculator.ui.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.calculator.business.manager.CalculatorManager
import com.example.calculator.business.manager.ICalculatorManager
import com.example.calculator.business.repository.IHistoryRepository
import io.reactivex.rxkotlin.Flowables
import io.reactivex.rxkotlin.subscribeBy
import java.lang.IllegalArgumentException

interface IHistoryViewModel {
    val computeUpdate: LiveData<String>
    val resultUpdate: LiveData<String>
    val items: LiveData<List<HistoryItemViewModel>>
    fun onClear()
}

class HistoryViewModel(private val historyRepository: IHistoryRepository, private val calculatorManager: ICalculatorManager) : IHistoryViewModel, ViewModel() {

    override val items: MutableLiveData<List<HistoryItemViewModel>> by lazy { MutableLiveData<List<HistoryItemViewModel>>() }

    override val computeUpdate: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    override val resultUpdate: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    init {
        historyRepository.histories.subscribeBy(onNext = { list ->
            items.postValue(list.map { history ->
                HistoryItemViewModel(
                    history.compute,
                    history.result,
                    { calculatorManager.setSelectedHistory(history.compute) },
                    { calculatorManager.setSelectedHistory(history.result) }
                )
            })
        })
    }

    override fun onClear() {
        historyRepository.deleteAll().subscribeBy(onError = { Log.e("history", "Delete history failed") }, onComplete = {})
    }


}

class HistoryViewModelFactory(private val historyRepository: IHistoryRepository,private val calculatorManager: ICalculatorManager) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java))
            return HistoryViewModel(historyRepository,calculatorManager) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}