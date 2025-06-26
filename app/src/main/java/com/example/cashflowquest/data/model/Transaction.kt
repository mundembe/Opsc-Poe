package com.example.cashflowquest.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val accountId: Long,
    val amount: Double,
    val type: String, // "Income", "Expense", or "Transfer"
    val category: String,
    val note: String,
    val timestamp: Long = System.currentTimeMillis()
)


