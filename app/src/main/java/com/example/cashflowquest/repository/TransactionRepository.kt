package com.example.cashflowquest.repository

import androidx.lifecycle.LiveData
import com.example.cashflowquest.data.dao.AccountDao
import com.example.cashflowquest.data.dao.TransactionDao
import com.example.cashflowquest.data.model.Account
import com.example.cashflowquest.data.model.Transaction

class TransactionRepository(
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao
) {
    fun getTransactionsForAccount(accountId: Long): LiveData<List<Transaction>> {
        return transactionDao.getTransactionsForAccount(accountId)
    }

    suspend fun insert(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }

    suspend fun update(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)
    }

    suspend fun delete(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }

    suspend fun getAccountById(accountId: Long): Account {
        return accountDao.getAccountById(accountId)
    }

    suspend fun updateAccount(account: Account) {
        accountDao.update(account)
    }
}
