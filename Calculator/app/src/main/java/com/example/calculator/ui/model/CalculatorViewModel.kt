package com.example.calculator.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.rxkotlin.subscribeBy
import android.text.SpannableString
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.core.graphics.toColor
import com.example.calculator.R
import com.example.calculator.business.manager.ICalculatorManager
import com.example.calculator.business.model.Entity
import com.example.calculator.business.model.History
import com.example.calculator.business.repository.IHistoryRepository
import com.example.calculator.ui.model.event.SingleLiveEvent
import com.example.calculator.util.factorial
import com.example.calculator.util.toSpannableString
import net.objecthunter.exp4j.ExpressionBuilder


interface ICalculatorViewModel {
    val compute: LiveData<String>
    val result: LiveData<String>
    val error: LiveData<Boolean>
    val enable: LiveData<Boolean>
    val history: LiveData<Boolean>
    val ee: SingleLiveEvent<Void>
    fun onHistory()
    fun onEE()
    fun onBack()
}

class CalculatorViewModel(private val historyRepository: IHistoryRepository, calculatorManager: ICalculatorManager) :
    ICalculatorViewModel, ViewModel() {

    override val compute: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    override val result: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    override val error: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    override val enable: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    override val history: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    override val ee: SingleLiveEvent<Void> by lazy { SingleLiveEvent<Void>() }

    private var historySwitch = true
    private var operator: Boolean = false
    private var equal: Boolean = false
    private var parentheses = 0


    init {
        calculatorManager.getSelectedInput().subscribeBy(onNext = { entity -> itemClicked(entity) })
        calculatorManager.getSelectedHistory().subscribeBy(onNext = { historyClicked(it) })
    }

    private fun itemClicked(entity: Entity) {
        when {
            entity.type == Entity.Type.EQUAL -> onEqual()
            entity.type == Entity.Type.CLEAR -> onClear()
            entity.type == Entity.Type.PARENTHESES -> onParentheses()
            entity.type == Entity.Type.TRIGO -> onScientificKeys(entity)
            entity.type == Entity.Type.PI || entity.type == Entity.Type.E -> onSpecialCharacters(entity)
            entity.type == Entity.Type.POW -> onPower(entity)
            entity.type == Entity.Type.MULTIPLE -> onXBehind(entity)
            entity.type == Entity.Type.FACT -> onFactorial()
            entity.value.matches(regexOperator) && compute.value.isNullOrEmpty() -> { }
            entity.value.matches(regexOperator) && compute.value != null -> onOperator(entity)
            !entity.value.matches(regexOperator) && operator -> onTextAfterOperator(entity)
            else -> onInput(entity)
        }
    }

    private fun onInput(entity: Entity) {
        var old = compute.value ?: ""
        if (equal) {
            old = ""
            equal = false
        } else if (old.isNotEmpty() && old.last() == ')')
            old = "$old*"
        compute.postValue(old + entity.value)
        enable.postValue(true)
    }

    private fun onTextAfterOperator(entity: Entity) {
        var old = compute.value ?: ""
        if (old.last() == ')')
            old += "*"
        old += entity.value
        compute.postValue(old)
        old = addParentheses(old)
        evaluate(old)
        enable.postValue(true)
    }

    private fun onXBehind(entity: Entity) {
        var old = compute.value ?: ""
        if (old.isNotEmpty() && old.last().isDigit())
            old = "$old*"
        old += entity.value.dropLast(1)
        if (entity.value.endsWith('^')) {
            old = "$old("
            parentheses++
        }
        compute.postValue(old)
        enable.postValue(true)
        operator = true
    }

    private fun onOperator(entity: Entity) {
        var old = compute.value!!
        operator = true
        if (old.last() == '(' && !(entity.value == "-" || entity.value == "+")) {
        } else if (!(old.last().toString().matches(regexOperator) && old.last().toString() == entity.value)) {
            if (old.last().toString().matches(regexOperator) && old.last().toString() != entity.value) {
                old.dropLast(1)
            }
            old += entity.value
            compute.postValue(old)
            enable.postValue(true)
        }
    }

    private fun onFactorial() {
        var old = compute.value ?: ""
        if (old.isNotEmpty()) {
            if (old.last().isDigit()) {
                old = "$old!"
                compute.postValue(old)
                val resultE = ExpressionBuilder(old)
                    .operator(factorial())
                    .build()
                    .evaluate()
                result.postValue(resultE.toString())
                enable.postValue(true)
            } else {
                error.postValue(true)
            }
        }
    }

    private fun onScientificKeys(entity: Entity) {
        val value = if (entity.value === "ctg") "1/tan"
        else entity.value
        var old = compute.value ?: ""
        if (old.isNotEmpty() && old.last().isDigit())
            old = "$old*"
        old += "$value("
        compute.postValue(old)
        enable.postValue(true)
        operator = true
        parentheses++
    }

    private fun onPower(entity: Entity) {
        var old = compute.value ?: ""
        if (old.isNotEmpty()) {
            if (old.last().isDigit() || old.takeLast(2) == "pi" || old.last() == 'e') {
                old = "$old^("
                if (entity.value.last().isDigit()) {
                    old += entity.value.last() + ")"
                    evaluate(old)
                } else
                    parentheses++
                compute.postValue(old)
                enable.postValue(true)
            } else {
                error.postValue(true)
            }
        }
    }

    private fun onSpecialCharacters(entity: Entity) {
        var old = compute.value ?: ""
        old += if (old.isEmpty() || old.last() == '(')
            entity.value
        else
            "*" + entity.value
        compute.postValue(old)
        evaluate(old)
        enable.postValue(true)
    }

    private fun onParentheses() {
        var old = compute.value ?: ""
        if (old.isEmpty() || old.last() == '(' || old.last().toString().matches(regexOperator)) {
            old = "$old("
            parentheses++
        } else if (parentheses > 0) {
            old = "$old)"
            parentheses--
        } else {
            old = "$old*("
            operator = true
            parentheses++
        }
        compute.postValue(old)
        enable.postValue(true)
    }

    private fun onClear() {
        operator = false
        parentheses = 0
        compute.postValue("")
        result.postValue("")
        enable.postValue(false)
    }

    private fun onEqual() {
        if (compute.value != null) {
            var old = compute.value!!
            if (old.last().toString().matches(regexOperator))
                error.postValue(true)
            else if (operator) {
                old = addParentheses(old)
                val resultC = ExpressionBuilder(old).build().evaluate().toString()
                historyRepository.addHistory(History(old, resultC))
                    .subscribeBy(onError = { Log.e("history", "Add history failed") },
                        onComplete = { Log.e("history", "Add history success") })
                compute.postValue(resultC)
                result.postValue("")
                operator = false
                parentheses = 0
                equal = true
            }
        }
    }

    private fun addParentheses(text: String): String {
        var old = text
        var continueP = parentheses
        while (continueP > 0) {
            old = "$old)"
            continueP--
        }
        return old
    }

    private fun evaluate(expr: String) {
        val expression = ExpressionBuilder(expr).build()
        result.postValue(expression.evaluate().toString())

    }

    override fun onBack() {
        if (compute.value!!.last() == ')') {
            parentheses++
        }
        if (compute.value!!.last() == '(')
            parentheses--
        val value = compute.value!!.dropLast(1)
        compute.postValue(value)
        result.postValue("")
        enable.postValue(value.isNotEmpty())
        operator = value.matches(regexExpression)
    }

    override fun onHistory() {
        history.postValue(historySwitch)
        historySwitch = !historySwitch
    }

    override fun onEE() {
        ee.postValue(null)
    }

    private fun historyClicked(value: String) {
        val old = compute.value ?: ""
        compute.postValue(old + value)
        enable.postValue(true)
    }

    companion object {
        val regexOperator = "[+-/*%]".toRegex()
        val regexExpression = ".*[+-/*%]+.*".toRegex()
    }

}

class CalculatorViewModelFactory(
    private val historyRepository: IHistoryRepository,
    private val calculatorManager: ICalculatorManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalculatorViewModel::class.java)) {
            return CalculatorViewModel(historyRepository, calculatorManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}