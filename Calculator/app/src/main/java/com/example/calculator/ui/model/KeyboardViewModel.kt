package com.example.calculator.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.calculator.R
import com.example.calculator.business.manager.ICalculatorManager
import com.example.calculator.business.model.Entity
import io.reactivex.rxkotlin.subscribeBy

interface IKeyboardViewModel {
    val items: LiveData<List<INumberViewModel>>
    val progress: LiveData<Boolean>
}

open class KeyboardViewModel(private val manager: ICalculatorManager) : IKeyboardViewModel, ViewModel() {

    override val items: MutableLiveData<List<INumberViewModel>> by lazy { MutableLiveData<List<INumberViewModel>>() }

    final override val progress: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    init {
        progress.postValue(true)
        manager.getNumbers().subscribeBy(onSuccess = { inputs ->
            mapInput(inputs.sortedBy { it.order })
            progress.postValue(false)
        }, onError = {
            mapInput(manager.getDummyNumbers().sortedBy { it.order })
            progress.postValue(false)
        })
    }

    protected fun mapInput(inputs: List<Entity>) {
        val result =
            inputs.map { entity ->
                when {
                    entity.type == Entity.Type.CLEAR -> NumberViewModel(
                        entity,
                        R.color.red,
                        R.color.white
                    ) { manager.setSelectedInput(entity) }
                    entity.type == Entity.Type.EQUAL -> NumberViewModel(
                        entity,
                        R.color.white,
                        R.color.light_sky_blue
                    ) { manager.setSelectedInput(entity) }
                    entity.type == Entity.Type.OPERATOR -> NumberViewModel(
                        entity,
                        R.color.light_sky_blue,
                        R.color.white
                    ) { manager.setSelectedInput(entity) }
                    else -> NumberViewModel(entity, R.color.black, R.color.white) { manager.setSelectedInput(entity) }
                }
            }
        items.postValue(result)
    }

}

class KeyboardViewModelFactory(private val manager: ICalculatorManager) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KeyboardViewModel::class.java)) {
            return KeyboardViewModel(manager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}