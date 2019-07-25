package com.example.calculator.business.dependencies

import android.content.Context
import androidx.room.Room
import com.example.calculator.business.database.ApplicationDatabase
import com.example.calculator.business.database.HistoryDao
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Reusable
    internal fun provideCalculatorDao(applicationDatabase: ApplicationDatabase): HistoryDao {
        return applicationDatabase.historyDao()
    }

    @Provides
    @Singleton
    internal fun provideDatabase(context: Context): ApplicationDatabase {
        return Room.databaseBuilder(context, ApplicationDatabase::class.java, DB_NAME).fallbackToDestructiveMigration().build()
    }

    companion object {
        const val DB_NAME = "history.db"
    }
}