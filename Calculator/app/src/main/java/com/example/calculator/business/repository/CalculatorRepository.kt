package com.example.calculator.business.repository

import com.example.calculator.business.model.Entity
import com.example.calculator.business.network.CalculatorClient
import io.reactivex.Single

interface ICalculatorRepository {
    fun getNumbers(): Single<List<Entity>>
    fun getOperators(): Single<List<Entity>>
    fun getScientific(howMany: Int): Single<List<Entity>>
    fun getDummyNumbers(): List<Entity>
    fun getDummyOperators(): List<Entity>
    fun getDummyScientific(howMany: Int): List<Entity>
}


class CalculatorRepository(private val calculatorClient: CalculatorClient) : ICalculatorRepository {
    override fun getOperators(): Single<List<Entity>> {
        return calculatorClient.getOperators()
    }

    override fun getNumbers(): Single<List<Entity>> {
        return calculatorClient.getNumbers()
    }

    override fun getScientific(howMany: Int): Single<List<Entity>> {
        return calculatorClient.getScientific(howMany)
    }

    override fun getDummyOperators(): List<Entity> {
        return listOf(
            Entity("C", 1, Entity.Type.CLEAR),
            Entity("()", 2, Entity.Type.OPERATOR),
            Entity("%", 3, Entity.Type.OPERATOR),
            Entity("/", 4, Entity.Type.OPERATOR),
            Entity("-", 5, Entity.Type.OPERATOR),
            Entity("*", 6, Entity.Type.OPERATOR),
            Entity("+", 7, Entity.Type.OPERATOR),
            Entity("=", 8, Entity.Type.OPERATOR)
        )
    }

    override fun getDummyNumbers(): List<Entity> {
        return listOf(
            Entity("C", 4, Entity.Type.CLEAR),
            Entity("()", 5, Entity.Type.PARENTHESES),
            Entity("%", 6, Entity.Type.OPERATOR),
            Entity("/", 7, Entity.Type.OPERATOR),

            Entity("7", 11, Entity.Type.NORMAL),
            Entity("8", 12, Entity.Type.NORMAL),
            Entity("9", 13, Entity.Type.NORMAL),
            Entity("*", 14, Entity.Type.OPERATOR),

            Entity("4", 18, Entity.Type.NORMAL),
            Entity("5", 19, Entity.Type.NORMAL),
            Entity("6", 20, Entity.Type.NORMAL),
            Entity("-", 21, Entity.Type.OPERATOR),

            Entity("1", 25, Entity.Type.NORMAL),
            Entity("2", 26, Entity.Type.NORMAL),
            Entity("3", 27, Entity.Type.NORMAL),
            Entity("+", 28, Entity.Type.OPERATOR),

            Entity("0", 32, Entity.Type.NORMAL),
            Entity(".", 33, Entity.Type.NORMAL),
            Entity("", 34, Entity.Type.NORMAL),
            Entity("=", 35, Entity.Type.EQUAL)
        )
    }

    override fun getDummyScientific(howMany: Int): List<Entity> {
        return listOf(
            Entity("sqrt", 1, Entity.Type.TRIGO),
            Entity("sin", 2, Entity.Type.TRIGO),
            Entity("cos", 3, Entity.Type.TRIGO),

            Entity("tan", 8, Entity.Type.TRIGO),
            Entity("ctg", 9, Entity.Type.TRIGO),
            Entity("log", 10, Entity.Type.TRIGO),

            Entity("log10", 15, Entity.Type.TRIGO),
            Entity("1/x", 16, Entity.Type.MULTIPLE),
            Entity("e^x", 17, Entity.Type.MULTIPLE),

            Entity("x^2", 22, Entity.Type.POW),
            Entity("x^3", 23, Entity.Type.POW),
            Entity("x^y", 24, Entity.Type.POW),

            Entity("pi", 29, Entity.Type.PI),
            Entity("e", 30, Entity.Type.E),
            Entity("x!", 31, Entity.Type.FACT)
        )
    }

}