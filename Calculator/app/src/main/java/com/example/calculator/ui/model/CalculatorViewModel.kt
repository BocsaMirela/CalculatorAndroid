package com.example.calculator.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.rxkotlin.subscribeBy
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import android.text.SpannableString
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import com.example.calculator.business.model.Entity
import com.example.calculator.business.model.History
import com.example.calculator.business.repository.IHistoryRepository
import com.example.calculator.ui.model.event.SingleLiveEvent
import com.example.calculator.util.factorial
import com.example.calculator.util.toSpannableString
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import net.objecthunter.exp4j.operator.Operator


interface ICalculatorViewModel {
    val compute: LiveData<SpannableString>
    val result: LiveData<String>
    val error: LiveData<Boolean>
    val enable: LiveData<Boolean>
    val history: LiveData<Boolean>
    val ee: SingleLiveEvent<Void>
    fun onUpdate(entity: Entity)
    fun onHistory()
    fun onEE()
    fun onBack()
    fun historyClicked(value: String)
}

class CalculatorViewModel(private val historyRepository: IHistoryRepository) : ICalculatorViewModel, ViewModel() {

    override val compute: MutableLiveData<SpannableString> by lazy { MutableLiveData<SpannableString>() }

    override val result: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    override val error: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    override val enable: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    override val history: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    override val ee: SingleLiveEvent<Void> by lazy { SingleLiveEvent<Void>() }

    private var historySwitch = true
    private var operator: Boolean = false
    private var equal: Boolean = false
    private var parentheses = 0

    override fun onUpdate(entity: Entity) {
        when {
            entity.type == Entity.Type.EQUAL -> {
                if (compute.value != null) {
                    val old = compute.value!!
                    if (old.last().toString().matches(regex))
                        error.postValue(true)
                    else if (operator) {
                        val resultC = ExpressionBuilder(old.toString()).build().evaluate().toString()
                        val spannedText = SpannableString(resultC).apply {
                            setSpan(
                                ForegroundColorSpan(Color.parseColor("#1874CD")),
                                0,
                                resultC.length,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                        historyRepository.addHistory(History(old.toString(), resultC))
                            .subscribeBy(onError = { Log.e("history", "Add history failed") },
                                onComplete = { Log.e("history", "Add history success") })
                        compute.postValue(spannedText)
                        result.postValue("")
                        operator = false
                        parentheses = 0
                        equal = true
                    }
                }
            }
            entity.type == Entity.Type.CLEAR -> {
                operator = false
                parentheses = 0
                compute.postValue("".toSpannableString())
                result.postValue("")
                enable.postValue(false)
            }
            entity.type == Entity.Type.PARENTHESES -> {
                val old = compute.value ?: ""
                val text = SpannableStringBuilder(old)
                if (old.isEmpty() || old.last() == '(' || old.last().toString().matches(regex)) {
                    text.append("(")
                    parentheses++
                } else if (parentheses > 0) {
                    text.append(")")
                    parentheses--
                } else {
                    text.append("*(")
                    operator = true
                    parentheses++
                }
                compute.postValue(SpannableString(text))
                enable.postValue(true)
            }
            entity.type == Entity.Type.TRIGO -> {
                val value = if (entity.value === "ctg") "1/tan"
                else entity.value
                val old = compute.value ?: ""
                val text = SpannableStringBuilder(old)
                if (old.isNotEmpty() && old.last().toString().matches(regex))
                    text.append("*")
                text.append(value).append("(")
                compute.postValue(SpannableString(text))
                enable.postValue(true)
                operator = true
                parentheses++
            }
            entity.type == Entity.Type.PI || entity.type == Entity.Type.E -> {
                val old = compute.value ?: ""
                val text = SpannableStringBuilder(old)
                if (old.isEmpty() || old.last() == '(')
                    text.append(entity.value)
                else
                    text.append("*").append(entity.value)
                compute.postValue(SpannableString(text))
                evaluate(text.toString())
                enable.postValue(true)

            }
            entity.type == Entity.Type.POW -> {
                val old = compute.value ?: ""
                val text = SpannableStringBuilder(old)
                if (old.isNotEmpty()) {
                    if (old.last().isDigit() || old.takeLast(2) == "pi" || old.last() == 'e') {
                        text.append("^(")
                        if (entity.value.last().isDigit()) {
                            text.append(entity.value.last()).append(")")
                            evaluate(text.toString())
                        } else
                            parentheses++
                        compute.postValue(SpannableString(text))
                        enable.postValue(true)
                    } else {
                        error.postValue(true)
                    }
                }

            }
            entity.type == Entity.Type.MULTIPLE -> {
                val old = compute.value ?: ""
                val text = SpannableStringBuilder(old)
                if (old.isNotEmpty() && old.last().isDigit())
                    text.append("*")
                text.append(entity.value.dropLast(1))
                if (entity.value.endsWith('^')) {
                    text.append("(")
                    parentheses++
                }
                compute.postValue(SpannableString(text))
                enable.postValue(true)
                operator = true
            }
            entity.type == Entity.Type.FACT -> {
                val old = compute.value ?: ""
                val text = SpannableStringBuilder(old)
                if (old.isNotEmpty()) {
                    if (old.last().isDigit()) {
                        text.append("!")
                        compute.postValue(SpannableString(text))
                        val resultE = ExpressionBuilder(text.toString())
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
            entity.value.matches(regex) && compute.value.isNullOrEmpty() -> {
            }
            entity.value.matches(regex) && compute.value != null -> {
                val old = compute.value!!
                operator = true
                if (old.last() == '(' && !(entity.value == "-" || entity.value == "+")) {
                } else if (!(old.last().toString().matches(regex) && old.last().toString() == entity.value)) {
                    val text = if (old.last().toString().matches(regex) && old.last().toString() != entity.value) {
                        SpannableStringBuilder(old.dropLast(1))
                    } else {
                        SpannableStringBuilder(old)
                    }
                    val spannedText = SpannableString(entity.value).apply {
                        setSpan(
                            ForegroundColorSpan(Color.parseColor("#1874CD")),
                            0,
                            1,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                    text.append(spannedText)
                    compute.postValue(SpannableString(text))
                    enable.postValue(true)
                }
            }
            !entity.value.matches(regex) && operator -> {
                val old = compute.value ?: ""
                val text = SpannableStringBuilder(compute.value!!)
                if (old.last() == ')')
                    text.append("*")
                text.append(entity.value)
                compute.postValue(SpannableString(text))
                var continueP = parentheses
                while (continueP > 0) {
                    text.append(")")
                    continueP--
                }
                evaluate(text.toString())
                enable.postValue(true)

            }
            else -> {
                val old = compute.value ?: ""
                var text = SpannableStringBuilder(old)
                if (equal) {
                    text = SpannableStringBuilder("")
                    equal = false
                } else if (old.toString().isNotEmpty() && old.last() == ')')
                    text.append("*")
                compute.postValue(SpannableString(text.append(entity.value)))
                enable.postValue(true)
            }
        }
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
        compute.postValue(SpannableString(value))
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

    override fun historyClicked(value: String) {
        val old = compute.value ?: ""
        compute.postValue(SpannableString(SpannableStringBuilder(old).append(value)))
        enable.postValue(true)
    }

    companion object {
        val regex = "[+-/*%]".toRegex()
        val regexExpression = ".*[+-/*%]+.*".toRegex()
    }

}

class CalculatorViewModelFactory(private val historyRepository: IHistoryRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalculatorViewModel::class.java)) {
            return CalculatorViewModel(historyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}