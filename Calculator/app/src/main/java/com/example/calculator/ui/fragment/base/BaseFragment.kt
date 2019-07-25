package com.example.calculator.ui.fragment.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.calculator.CalculatorApplication

abstract class BaseFragment : Fragment() {

    protected var savedState: Bundle? = null
    private val BASE = javaClass.simpleName

    override fun onAttach(context: Context) {
        (context.applicationContext as CalculatorApplication).applicationComponent.inject(this)
        super.onAttach(context)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedState = savedInstanceState
    }

}