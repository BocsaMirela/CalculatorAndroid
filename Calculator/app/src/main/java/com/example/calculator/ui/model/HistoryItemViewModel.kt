package com.example.calculator.ui.model

interface IHistoryItemViewModel {
    val compute: String
    val result: String
    fun onComputeClick()
    fun onResultClick()
}

class HistoryItemViewModel(override val compute: String, equal: String,
    private val computeClick: () -> Unit, private val resultClick: () -> Unit) : IHistoryItemViewModel {

    override val result: String = "=$equal"

    override fun onComputeClick() {
        computeClick.invoke()
    }

    override fun onResultClick() {
        resultClick.invoke()
    }
}