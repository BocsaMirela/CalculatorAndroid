package com.example.calculator.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.calculator.R
import com.example.calculator.databinding.HistoryItemBinding
import com.example.calculator.ui.adapter.base.BindingBaseAdapter
import com.example.calculator.ui.adapter.holder.HistoryItemViewHolder
import com.example.calculator.ui.model.IHistoryItemViewModel

class HistoryAdapter(private val context: Context) :
    BindingBaseAdapter<IHistoryItemViewModel, HistoryItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        val binding = DataBindingUtil.inflate<HistoryItemBinding>(
            LayoutInflater.from(context),
            R.layout.history_item,
            parent,
            false
        )
        return HistoryItemViewHolder(binding)
    }


}