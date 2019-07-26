package com.example.calculator.ui.activity

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.calculator.CalculatorApplication
import com.example.calculator.R
import com.example.calculator.business.manager.CalculatorManager
import com.example.calculator.business.manager.ICalculatorManager
import com.example.calculator.business.model.Entity
import com.example.calculator.business.repository.IHistoryRepository
import com.example.calculator.databinding.ActivityMainBinding
import com.example.calculator.ui.fragment.*
import com.example.calculator.ui.model.CalculatorViewModel
import com.example.calculator.ui.model.CalculatorViewModelFactory
import com.example.calculator.ui.model.ICalculatorViewModel
import org.jetbrains.anko.toast
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    internal lateinit var historyRepository: IHistoryRepository

    @Inject
    internal lateinit var calculatorManager: ICalculatorManager

    private val viewModel: ICalculatorViewModel by lazy {
        val factory = CalculatorViewModelFactory(historyRepository, calculatorManager)
        ViewModelProvider(this, factory).get(CalculatorViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as CalculatorApplication).applicationComponent.inject(this)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        when {
            portrait() ->
                supportFragmentManager.beginTransaction().replace(
                    R.id.container,
                    KeyboardFragment().apply { arguments = intent.extras },
                    null
                ).commit()
            else ->
                supportFragmentManager.beginTransaction().replace(
                    R.id.container,
                    ScientificFragment().apply { arguments = intent.extras },
                    null
                ).commit()
        }

        viewModel.error.observe(this, Observer {
            applicationContext!!.toast(R.string.invalid)
        })

        viewModel.history.observe(this, Observer {
            when {
                it -> this.supportFragmentManager.beginTransaction().replace(R.id.container, HistoryFragment()).commit()
                else -> {
                    when {
                        portrait() ->
                            supportFragmentManager.beginTransaction().replace(
                                R.id.container,
                                KeyboardFragment().apply { arguments = intent.extras },
                                null
                            ).commit()
                        else ->
                            supportFragmentManager.beginTransaction().replace(
                                R.id.container,
                                ScientificFragment().apply { arguments = intent.extras },
                                null
                            ).commit()
                    }
                }
            }
        })

        viewModel.ee.observe(this, Observer {
            requestedOrientation = when {
                portrait() -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                else -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        })
    }

    private fun portrait(): Boolean {
        return this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_COMPUTE, viewModel.compute.value)
        outState.putString(KEY_RESULT, viewModel.result.value)
    }

    companion object {
        private const val KEY_COMPUTE = "COMPUTE"
        private const val KEY_RESULT = "RESULT"
    }
}