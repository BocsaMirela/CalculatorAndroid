package com.example.calculator.ui.model.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator.ui.adapter.base.IBindingAdapter

object RecyclerViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("android:items")
    fun setItems(view: RecyclerView, items: List<Any>?) {
        val adapter = view.adapter
        if (adapter == null || items == null)
            return
        if (adapter is IBindingAdapter) {
            adapter.bindItems(items)
        }
    }
}