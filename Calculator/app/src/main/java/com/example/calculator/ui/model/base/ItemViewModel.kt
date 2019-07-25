package com.example.calculator.ui.model.base

interface IItemViewModel<in T> {
    val itemClick: (item: T) -> Unit
    fun onClick()
}