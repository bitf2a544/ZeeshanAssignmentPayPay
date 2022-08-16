package com.example.zeeshanassignmentpaypay.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.zeeshanassignmentpaypay.data.model.Currency

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(currency: Currency)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencyList(currencyList: List<Currency>)

    @Delete
    suspend fun deleteCurrency(currency: Currency)

    @Query("SELECT * from Currency")
    suspend fun getAllCurrencyList(): List<Currency>

    @Query("SELECT * from Currency")
    fun observeAllAllCurrencyItems(): LiveData<List<Currency>>

}