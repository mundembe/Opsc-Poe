package com.example.cashflowquest.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashflowquest.data.dao.AccountDao
import com.example.cashflowquest.data.dao.TransactionDao
import com.example.cashflowquest.data.database.AppDatabase
import com.example.cashflowquest.data.model.Transaction
import com.example.cashflowquest.repository.TransactionRepository
import com.example.cashflowquest.ui.transaction.TransactionType
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao
) : ViewModel() {

    private val repository = TransactionRepository(transactionDao, accountDao)

    private val _accountId = MutableLiveData<Long>()
    private val _transactions = MediatorLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> get() = _transactions

    init {
        _transactions.addSource(_accountId) { accountId ->
            repository.getTransactionsForAccount(accountId).observeForever { txList ->
                _transactions.value = txList
            }
        }
    }

    fun insert(transaction: Transaction) {
        viewModelScope.launch {
            // Insert transaction
            transactionDao.insertTransaction(transaction)

            // Update account balance
            val account = accountDao.getAccountById(transaction.accountId)
            if (account != null) {
                val newBalance = if (transaction.type == TransactionType.INCOME.label) {
                    account.balance + transaction.amount
                } else {
                    account.balance - transaction.amount
                }
                accountDao.update(account.copy(balance = newBalance))
            }
        }
    }

    fun setAccountId(accountId: Long) {
        _accountId.value = accountId
    }

    fun update(transaction: Transaction) = viewModelScope.launch {
        transactionDao.updateTransaction(transaction)
    }

    fun delete(transaction: Transaction) = viewModelScope.launch {
        transactionDao.deleteTransaction(transaction)
    }
}