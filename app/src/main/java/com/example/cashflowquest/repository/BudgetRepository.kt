package com.example.cashflowquest.repository

import androidx.lifecycle.LiveData
import com.example.cashflowquest.data.dao.BudgetDao
import com.example.cashflowquest.data.model.Budget

class BudgetRepository(private val budgetDao: BudgetDao) {
    val allBudgets: LiveData<List<Budget>> = budgetDao.getAllBudgets()

    suspend fun insert(budget: Budget) = budgetDao.insert(budget)

    suspend fun updateSpending(category: String, amount: Double) {
        budgetDao.updateSpending(category, amount)
    }

    suspend fun deleteBudget(budgetId: Int) {
        budgetDao.deleteBudget(budgetId)
    }

    fun getBudgetById(id: Int): LiveData<Budget?> = budgetDao.getBudgetById(id)
}