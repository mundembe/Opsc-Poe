package com.example.cashflowquest.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cashflowquest.data.dao.AccountDao
import com.example.cashflowquest.data.dao.BudgetDao
import com.example.cashflowquest.data.dao.TransactionDao
import com.example.cashflowquest.data.model.Account
import com.example.cashflowquest.data.model.Budget
import com.example.cashflowquest.data.model.Transaction

@Database(
    entities = [Account::class, Transaction::class, Budget::class],
    version = 4,  // Update this if schema changes
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cashflow_database" // Use one consistent DB name
                )
                    .fallbackToDestructiveMigration() // Dev-safe: wipes and rebuilds on schema change
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
