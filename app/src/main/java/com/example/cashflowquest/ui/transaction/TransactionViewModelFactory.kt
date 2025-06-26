package com.example.cashflowquest.ui.transaction

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cashflowquest.data.dao.AccountDao
import com.example.cashflowquest.data.dao.TransactionDao
import com.example.cashflowquest.viewmodel.TransactionViewModel

class TransactionViewModelFactory(
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            return TransactionViewModel(transactionDao, accountDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


