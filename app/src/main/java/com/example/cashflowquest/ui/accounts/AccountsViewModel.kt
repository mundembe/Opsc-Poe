package com.example.cashflowquest.ui.accounts

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashflowquest.data.dao.AccountDao
import com.example.cashflowquest.data.dao.TransactionDao
import com.example.cashflowquest.data.database.AppDatabase
import com.example.cashflowquest.data.model.Account
import com.example.cashflowquest.data.model.Transaction
import com.example.cashflowquest.repository.AccountsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountsViewModel(
    private val accountDao: AccountDao,
    private val transactionDao: TransactionDao
) : ViewModel() {

    val allAccounts: LiveData<List<Account>> = accountDao.getAllAccounts()

    fun insert(account: Account) = viewModelScope.launch {
        accountDao.insert(account)
    }

    fun insertTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionDao.insertTransaction(transaction)
            updateAccountBalance(transaction.accountId)
        }
    }


    private suspend fun updateAccountBalance(accountId: Long) {
        val transactions = transactionDao.getTransactionsForAccountNow(accountId) // Should return List<Transaction>
        val newBalance = transactions.sumOf {
            if (it.type.equals("Income", ignoreCase = true)) it.amount else -it.amount
        }
        accountDao.updateBalance(accountId, newBalance)
        Log.d("debug", "Updating balance for account $accountId")
        Log.d("debug", "New balance: $newBalance")
    }





}
