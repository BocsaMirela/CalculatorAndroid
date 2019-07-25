package com.example.calculator.ui.model

interface IHistoryItemViewModel {
    val compute: String
    val result: String
    fun onComputeClick()
    fun onResultClick()
}

class HistoryItemViewModel(override val compute: String, private val equal: String,
    private val computeClick: (item: String) -> Unit, private val resultClick: (item: String) -> Unit) : IHistoryItemViewModel {

    override val result: String = "=$equal"

    override fun onComputeClick() {
        computeClick.invoke(compute)
    }

    override fun onResultClick() {
        resultClick.invoke(equal)
    }
}