package com.example.calculator.business.dependencies

import com.example.calculator.ui.activity.MainActivity
import com.example.calculator.ui.fragment.HistoryFragment
import com.example.calculator.ui.fragment.KeyboardFragment
import com.example.calculator.ui.fragment.ScientificFragment
import com.example.calculator.ui.fragment.base.BaseFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, RepositoryModule::class, ManagerModule::class, NetworkModule::class, DatabaseModule::class])
interface ApplicationComponent {
    fun inject(historyFragment: HistoryFragment)
    fun inject(scientificFragment: ScientificFragment)
    fun inject(baseFragment: BaseFragment)
    fun inject(keyboardFragment: KeyboardFragment)
    fun inject(activity: MainActivity)

}