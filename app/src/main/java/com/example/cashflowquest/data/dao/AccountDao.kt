package com.example.cashflowquest.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cashflowquest.data.model.Account

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: Account)

    @Query("SELECT * FROM accounts ORDER BY id DESC")
    fun getAllAccounts(): LiveData<List<Account>>

    @Query("SELECT * FROM accounts WHERE id = :accountId LIMIT 1")
    suspend fun getAccountById(accountId: Long): Account

    @Delete
    suspend fun delete(account: Account)

    @Update
    suspend fun update(account: Account)

    @Query("UPDATE accounts SET balance = :newBalance WHERE id = :accountId")
    suspend fun updateBalance(accountId: Long, newBalance: Double)


}
