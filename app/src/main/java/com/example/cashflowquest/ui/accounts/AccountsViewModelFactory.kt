package com.example.cashflowquest.ui.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cashflowquest.data.dao.AccountDao
import com.example.cashflowquest.data.dao.TransactionDao

class AccountsViewModelFactory(
    private val accountDao: AccountDao,
    private val transactionDao: TransactionDao
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountsViewModel::class.java)) {
            return AccountsViewModel(accountDao, transactionDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
