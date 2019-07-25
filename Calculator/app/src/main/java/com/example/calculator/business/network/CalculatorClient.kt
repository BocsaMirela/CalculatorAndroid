package com.example.calculator.business.network

import com.example.calculator.business.model.Entity
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface CalculatorClient {

    @GET("numbers")
    fun getNumbers(): Single<List<Entity>>

    @GET("science/{number}")
    fun getScientific(@Path("number") howMany: Int): Single<List<Entity>>

    @GET("operators")
    fun getOperators(): Single<List<Entity>>

}