package com.example.calculator.business.manager

import com.example.calculator.business.model.Entity
import com.example.calculator.business.repository.ICalculatorRepository
import io.reactivex.Single

interface ICalculatorManager {
    fun getNumbers(): Single<List<Entity>>
    fun getScientific(howMany: Int): Single<List<Entity>>
    fun getDummyNumbers():List<Entity>
    fun getDummyOperators(): List<Entity>
    fun getDummyScientific(howMany: Int):List<Entity>
}

class CalculatorManager (private val repository: ICalculatorRepository):ICalculatorManager{
    override fun getNumbers(): Single<List<Entity>> {
        return repository.getNumbers()
    }

    override fun getScientific(howMany: Int): Single<List<Entity>> {
        return repository.getScientific(howMany)
    }

    override fun getDummyOperators(): List<Entity> {
        return repository.getDummyOperators()
    }

    override fun getDummyNumbers(): List<Entity> {
        return repository.getDummyNumbers()
    }

    override fun getDummyScientific(howMany: Int): List<Entity> {
        return repository.getDummyScientific(howMany)
    }
}