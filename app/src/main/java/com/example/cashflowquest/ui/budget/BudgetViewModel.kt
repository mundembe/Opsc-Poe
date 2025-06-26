package com.example.cashflowquest.ui.budget

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.cashflowquest.data.database.AppDatabase
import com.example.cashflowquest.data.model.Budget
import com.example.cashflowquest.repository.BudgetRepository
import kotlinx.coroutines.launch

class BudgetViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BudgetRepository
    val allBudgets: LiveData<List<Budget>>
    val budgetProgress: LiveData<List<BudgetProgress>>
    private val _snackbarMessage = MutableLiveData<String?>()
    val snackbarMessage: LiveData<String?> get() = _snackbarMessage

    init {
        val budgetDao = AppDatabase.getInstance(application).budgetDao()
        repository = BudgetRepository(budgetDao)
        allBudgets = repository.allBudgets
        budgetProgress = allBudgets.map { budgets ->
            budgets.map { budget ->
                BudgetProgress(
                    id = budget.id,
                    category = budget.category,
                    progress = calculateProgress(budget.currentSpending, budget.limit),
                    remaining = budget.limit - budget.currentSpending,
                    isOverBudget = budget.currentSpending > budget.limit
                )
            }
        }
    }

    private fun calculateProgress(spent: Double, limit: Double): Float {
        return if (limit == 0.0) 0f else (spent / limit).coerceAtMost(1.0).toFloat()
    }

    fun createBudget(category: String, limit: Double) {
        if (category.isBlank()) {
            _snackbarMessage.value = "Category cannot be empty"
            return
        }

        if (limit <= 0) {
            _snackbarMessage.value = "Limit must be positive"
            return
        }

        viewModelScope.launch {
            try {
                repository.insert(Budget(category = category, limit = limit))
                _snackbarMessage.value = "Budget created successfully"
            } catch (e: Exception) {
                _snackbarMessage.value = "Error creating budget: ${e.message}"
            } finally {
                _snackbarMessage.value = null
            }
        }
    }

    fun updateBudgetSpending(category: String, amount: Double) {
        viewModelScope.launch {
            try {
                repository.updateSpending(category, amount)
            } catch (e: Exception) {
                _snackbarMessage.value = "Error updating budget: ${e.message}"
            }
        }
    }

    fun deleteBudget(budgetId: Int) {
        viewModelScope.launch {
            try {
                repository.deleteBudget(budgetId)
                _snackbarMessage.value = "Budget deleted"
            } catch (e: Exception) {
                _snackbarMessage.value = "Error deleting budget"
            }
        }
    }

    fun getBudgetById(id: Int): LiveData<Budget?> {
        return repository.getBudgetById(id)
    }

    fun getBudgetIdByCategory(category: String): Int? {
        return allBudgets.value?.firstOrNull {
            it.category.equals(category, ignoreCase = true)
        }?.id
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun refreshBudgets() {
        // For example, reload data from repository or remote source
        viewModelScope.launch {
            try {
                //repository.reloadBudgets() // make sure this exists
            } catch (e: Exception) {
                _snackbarMessage.value = "Error refreshing budgets: ${e.message}"
            }
        }
    }

    fun addTestData() {
        viewModelScope.launch {
            repository.insert(Budget(category = "Groceries", limit = 500.0))
            repository.insert(Budget(category = "Entertainment", limit = 200.0))
            repository.insert(Budget(category = "Transport", limit = 300.0))
        }
    }

}

data class BudgetProgress(
    val id: Int,  // Added to directly reference the budget
    val category: String,
    val progress: Float,
    val remaining: Double,
    val isOverBudget: Boolean
)