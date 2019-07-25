package com.example.calculator.ui.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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

class HistoryViewModel(private val historyRepository: IHistoryRepository) : IHistoryViewModel, ViewModel() {

    override val items: MutableLiveData<List<HistoryItemViewModel>> by lazy { MutableLiveData<List<HistoryItemViewModel>>() }

    override val computeUpdate: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    override val resultUpdate: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    private val resultClick: (String) -> Unit = {
        resultUpdate.postValue(it)
    }

    private val computeClick: (String) -> Unit = {
        computeUpdate.postValue(it)
    }

    init {
        historyRepository.histories.subscribeBy(onNext = { list ->
            Log.e("history", "gel all history success")
            items.postValue(list.map { history ->
                HistoryItemViewModel(
                    history.compute,
                    history.result,
                    computeClick,
                    resultClick
                )
            })
        })
    }

    override fun onClear() {
        historyRepository.deleteAll()
            .subscribeBy(onError = { Log.e("history", "Delete history failed") }, onComplete = {})
    }


}

class HistoryViewModelFactory(private val historyRepository: IHistoryRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java))
            return HistoryViewModel(historyRepository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}