package com.example.calculator.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator.CalculatorApplication
import com.example.calculator.R
import com.example.calculator.business.manager.ICalculatorManager
import com.example.calculator.databinding.KeyboardBinding
import com.example.calculator.ui.adapter.InputAdapter
import com.example.calculator.ui.fragment.base.BaseFragment
import com.example.calculator.ui.model.IKeyboardViewModel
import com.example.calculator.ui.model.ScientificViewModel
import com.example.calculator.ui.model.ScientificViewModelFactory
import com.grzegorzojdana.spacingitemdecoration.Spacing
import com.grzegorzojdana.spacingitemdecoration.SpacingItemDecoration
import javax.inject.Inject

class ScientificFragment : BaseFragment() {

    @Inject
    internal lateinit var manager: ICalculatorManager

    private val viewModel: IKeyboardViewModel by lazy {
        val factory = ScientificViewModelFactory(manager)
        ViewModelProvider(this, factory).get(ScientificViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        (context.applicationContext as CalculatorApplication).applicationComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<KeyboardBinding>(inflater, R.layout.keyboard, container, false)
        binding.lifecycleOwner = this
        binding.numbers.layoutManager = GridLayoutManager(context, 7, RecyclerView.VERTICAL, false)
        binding.numbers.adapter = InputAdapter(context!!)
        val spacing = resources.getDimensionPixelOffset(R.dimen.small_margin)
        binding.numbers.addItemDecoration(SpacingItemDecoration(Spacing(horizontal = spacing, vertical = spacing)))

        binding.viewModel = viewModel

        return binding.root
    }
}