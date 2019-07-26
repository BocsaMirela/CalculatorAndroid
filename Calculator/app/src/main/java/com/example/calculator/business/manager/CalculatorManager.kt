package com.example.calculator.business.manager

import com.example.calculator.business.model.Entity
import com.example.calculator.business.repository.ICalculatorRepository
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.processors.BehaviorProcessor

interface ICalculatorManager {
    fun getNumbers(): Single<List<Entity>>
    fun getScientific(): Single<List<Entity>>
    fun getDummyNumbers(): List<Entity>
    fun getDummyOperators(): List<Entity>
    fun getDummyScientific(): List<Entity>
    fun setSelectedInput(entity: Entity)
    fun getSelectedInput(): Flowable<Entity>
    fun setSelectedHistory(value: String)
    fun getSelectedHistory(): Flowable<String>
}

class CalculatorManager(private val repository: ICalculatorRepository) : ICalculatorManager {

    private val inputProcessor by lazy { BehaviorProcessor.create<Entity>() }

    private val historyProcessor by lazy { BehaviorProcessor.create<String>() }

    override fun getNumbers(): Single<List<Entity>> {
        return repository.getNumbers()
    }

    override fun getScientific(): Single<List<Entity>> {
        return repository.getScientific()
    }

    override fun getDummyOperators(): List<Entity> {
        return repository.getDummyOperators()
    }

    override fun getDummyNumbers(): List<Entity> {
        return repository.getDummyNumbers()
    }

    override fun getDummyScientific(): List<Entity> {
        return repository.getDummyScientific()
    }

    override fun getSelectedInput(): Flowable<Entity> {
        return inputProcessor
    }

    override fun setSelectedInput(entity: Entity) {
        inputProcessor.onNext(entity)
    }

    override fun getSelectedHistory(): Flowable<String> {
        return historyProcessor
    }

    override fun setSelectedHistory(value: String) {
        historyProcessor.onNext(value)
    }
}