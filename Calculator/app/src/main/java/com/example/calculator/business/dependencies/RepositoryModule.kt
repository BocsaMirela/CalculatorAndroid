package com.example.calculator.business.dependencies

import com.example.calculator.business.database.HistoryDao
import com.example.calculator.business.network.CalculatorClient
import com.example.calculator.business.repository.CalculatorRepository
import com.example.calculator.business.repository.HistoryRepository
import com.example.calculator.business.repository.ICalculatorRepository
import com.example.calculator.business.repository.IHistoryRepository
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

@Module
class RepositoryModule {

    @Provides
    @Reusable
    internal fun provideCalculatorRepository(client: CalculatorClient): ICalculatorRepository {
        return CalculatorRepository(client)
    }

    @Provides
    @Reusable
    internal fun provideHistoryRepository(historyDao: HistoryDao): IHistoryRepository {
        return HistoryRepository(historyDao, Schedulers.single())
    }

}