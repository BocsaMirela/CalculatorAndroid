package com.example.calculator.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.calculator.R
import com.example.calculator.databinding.ItemBinding
import com.example.calculator.ui.adapter.base.BindingBaseAdapter
import com.example.calculator.ui.adapter.holder.NumberViewHolder
import com.example.calculator.ui.model.INumberViewModel

open class NumberAdapter(private val context: Context) : BindingBaseAdapter<INumberViewModel, NumberViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        val binding = DataBindingUtil.inflate<ItemBinding>(LayoutInflater.from(context),
            R.layout.item, parent, false)
        return NumberViewHolder(binding)
    }

}