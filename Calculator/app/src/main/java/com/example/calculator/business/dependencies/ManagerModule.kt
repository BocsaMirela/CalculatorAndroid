package com.example.calculator.business.dependencies

import com.example.calculator.business.manager.CalculatorManager
import com.example.calculator.business.manager.ICalculatorManager
import com.example.calculator.business.repository.ICalculatorRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ManagerModule {

    @Provides
    @Singleton
    internal fun provideCalculatorManager(repository: ICalculatorRepository): ICalculatorManager {
        return CalculatorManager(repository)
    }
}