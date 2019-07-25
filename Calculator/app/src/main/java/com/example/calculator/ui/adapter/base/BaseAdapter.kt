package com.example.calculator.ui.adapter.base

import androidx.recyclerview.widget.RecyclerView
import com.example.calculator.ui.adapter.holder.base.BaseViewHolder

abstract class BaseAdapter<T, VH : BaseViewHolder<T>> : RecyclerView.Adapter<VH>() {

    protected var items: MutableList<T>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return items?.size?:0
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bindData(items!![position])
    }

    protected fun getItem(position: Int): T {
        return items!![position]
    }

}