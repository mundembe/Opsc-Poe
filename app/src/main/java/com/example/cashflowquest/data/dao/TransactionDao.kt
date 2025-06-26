package com.example.cashflowquest.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cashflowquest.data.model.Account
import com.example.cashflowquest.data.model.Transaction

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions WHERE accountId = :accountId")
    fun getTransactionsForAccount(accountId: Long): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE accountId = :accountId")
    suspend fun getTransactionsForAccountNow(accountId: Long): List<Transaction>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT * FROM accounts WHERE id = :accountId LIMIT 1")
    suspend fun getAccountById(accountId: Long): Account

}


