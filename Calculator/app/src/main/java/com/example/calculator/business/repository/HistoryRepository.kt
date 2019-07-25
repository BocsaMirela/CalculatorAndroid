package com.example.calculator.business.repository

import com.example.calculator.business.database.HistoryDao
import com.example.calculator.business.model.History
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Scheduler

interface IHistoryRepository {
    val histories: Flowable<List<History>>
    fun addHistory(history: History): Completable
    fun deleteAll(): Completable
}

class HistoryRepository(private val historyDao: HistoryDao, private val scheduler: Scheduler) : IHistoryRepository {

    override fun addHistory(history: History): Completable = Completable.fromCallable { historyDao.insert(history) }.subscribeOn(scheduler)

    override fun deleteAll(): Completable = Completable.fromCallable { historyDao.clear() }.subscribeOn(scheduler)

    override val histories: Flowable<List<History>>
        get() = historyDao.histories.subscribeOn(scheduler)


}