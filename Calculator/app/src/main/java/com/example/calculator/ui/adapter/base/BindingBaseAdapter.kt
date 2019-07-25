package com.example.calculator.ui.adapter.base

import com.example.calculator.ui.adapter.holder.base.BaseViewHolder

abstract class BindingBaseAdapter<T, VH : BaseViewHolder<T>> : BaseAdapter<T, VH>(),
    IBindingAdapter {

    override fun bindItems(items: List<Any>) {
        this.items = items.toMutableList() as MutableList<T>
    }

}