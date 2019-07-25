package com.example.calculator.business.database

import androidx.room.*
import com.example.calculator.business.model.History
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(history: History)

    @get:Query("SELECT * FROM histories")
    val histories: Flowable<List<History>>

    @Delete
    fun delete(history: History)

    @Query("DELETE FROM histories")
    fun clear()
}