package com.example.calculator.business.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.calculator.business.model.History

@Database(entities = [History::class], version = 1, exportSchema = false)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}