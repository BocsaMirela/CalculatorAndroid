package com.example.calculator.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator.CalculatorApplication
import com.example.calculator.R
import com.example.calculator.business.manager.CalculatorManager
import com.example.calculator.business.manager.ICalculatorManager
import com.example.calculator.business.repository.IHistoryRepository
import com.example.calculator.databinding.HistoryBinding
import com.example.calculator.ui.adapter.HistoryAdapter
import com.example.calculator.ui.fragment.base.BaseFragment
import com.example.calculator.ui.model.HistoryViewModel
import com.example.calculator.ui.model.HistoryViewModelFactory
import com.example.calculator.ui.model.IHistoryViewModel
import com.grzegorzojdana.spacingitemdecoration.Spacing
import com.grzegorzojdana.spacingitemdecoration.SpacingItemDecoration
import javax.inject.Inject

class HistoryFragment : BaseFragment() {

    @Inject
    internal lateinit var historyRepository: IHistoryRepository

    @Inject
    internal lateinit var calculatorManager: ICalculatorManager

    val viewModel: IHistoryViewModel by lazy {
        val factory = HistoryViewModelFactory(historyRepository, calculatorManager)
        ViewModelProvider(this, factory).get(HistoryViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        (context.applicationContext as CalculatorApplication).applicationComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<HistoryBinding>(inflater, R.layout.history, container, false)
        binding.lifecycleOwner = this
        binding.histories.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        binding.histories.adapter = HistoryAdapter(context!!)
        binding.histories.addItemDecoration(SpacingItemDecoration(Spacing(horizontal = context!!.resources.getDimensionPixelOffset(
                        R.dimen.default_margin
                    ), vertical = context!!.resources.getDimensionPixelOffset(
                        R.dimen.default_margin
                    ))))
        binding.viewModel = viewModel
        return binding.root
    }
}

