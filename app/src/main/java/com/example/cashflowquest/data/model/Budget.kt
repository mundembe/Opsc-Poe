package com.example.cashflowquest.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,
    val limit: Double,
    val currentSpending: Double = 0.0,
    val period: String = "Monthly"  // Weekly, Monthly, Yearly
)