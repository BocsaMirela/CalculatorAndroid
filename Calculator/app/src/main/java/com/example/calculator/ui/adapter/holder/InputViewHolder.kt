package com.example.calculator.ui.adapter.holder

import com.example.calculator.databinding.ItemBinding
import com.example.calculator.ui.adapter.holder.base.BaseViewHolder
import com.example.calculator.ui.model.IInputViewModel


class InputViewHolder(private val binding: ItemBinding) : BaseViewHolder<IInputViewModel>(binding.root) {

    override fun bindData(item: IInputViewModel) {
        binding.viewModel = item
        binding.executePendingBindings()
    }
}