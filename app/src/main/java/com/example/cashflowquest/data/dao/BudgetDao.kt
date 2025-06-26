package com.example.cashflowquest.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cashflowquest.data.model.Budget

@Dao
interface BudgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: Budget)

    @Query("SELECT * FROM budgets")
    fun getAllBudgets(): LiveData<List<Budget>>

    @Query("SELECT * FROM budgets WHERE id = :id LIMIT 1")
    fun getBudgetById(id: Int): LiveData<Budget?>

    @Query("UPDATE budgets SET currentSpending = currentSpending + :amount WHERE category = :category")
    suspend fun updateSpending(category: String, amount: Double)

    @Query("DELETE FROM budgets WHERE id = :budgetId")
    suspend fun deleteBudget(budgetId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM budgets WHERE category = :category LIMIT 1)")
    suspend fun categoryExists(category: String): Boolean
}