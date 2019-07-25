package com.example.calculator.ui.adapter.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.calculator.ui.adapter.holder.base.BaseViewHolder

abstract class BaseDiffAdapter<T, VH : BaseViewHolder<T>>(itemCallback: DiffUtil.ItemCallback<T>) : ListAdapter<T, VH>(itemCallback),
    IBindingAdapter {

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bindData(getItem(position))
    }

    override fun bindItems(items: List<Any>) {
        submitList(items as List<T>)
    }
}