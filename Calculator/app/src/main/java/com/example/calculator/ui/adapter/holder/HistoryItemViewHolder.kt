package com.example.calculator.ui.adapter.holder

import com.example.calculator.databinding.HistoryItemBinding
import com.example.calculator.ui.adapter.holder.base.BaseViewHolder
import com.example.calculator.ui.model.IHistoryItemViewModel

class HistoryItemViewHolder(private val binding: HistoryItemBinding) : BaseViewHolder<IHistoryItemViewModel>(binding.root) {

    override fun bindData(item: IHistoryItemViewModel) {
        binding.viewModel = item
        binding.executePendingBindings()
    }

}