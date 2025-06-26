package com.example.cashflowquest.repository

import androidx.lifecycle.LiveData
import com.example.cashflowquest.data.dao.AccountDao
import com.example.cashflowquest.data.model.Account

class AccountsRepository(private val accountDao: AccountDao) {

    fun getAllAccounts(): LiveData<List<Account>> = accountDao.getAllAccounts()

    suspend fun insert(account: Account) = accountDao.insert(account)

    suspend fun delete(account: Account) = accountDao.delete(account)

    suspend fun getAccountById(accountId: Long): Account {
        return accountDao.getAccountById(accountId)
    }

    suspend fun updateAccount(account: Account) {
        accountDao.update(account)
    }

}
