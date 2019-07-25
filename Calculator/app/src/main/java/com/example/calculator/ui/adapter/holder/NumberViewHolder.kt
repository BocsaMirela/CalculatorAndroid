package com.example.calculator.ui.adapter.holder

import com.example.calculator.databinding.ItemBinding
import com.example.calculator.ui.adapter.holder.base.BaseViewHolder
import com.example.calculator.ui.model.INumberViewModel


class NumberViewHolder(private val binding: ItemBinding) : BaseViewHolder<INumberViewModel>(binding.root) {

    override fun bindData(item: INumberViewModel) {
        binding.viewModel = item
        binding.executePendingBindings()
    }
}