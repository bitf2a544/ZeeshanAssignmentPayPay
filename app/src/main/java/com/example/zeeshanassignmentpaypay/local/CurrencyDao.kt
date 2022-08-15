package com.example.zeeshanassignmentpaypay.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.zeeshanassignmentpaypay.data.model.Currency

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(user: Currency)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencyList(currencyList: List<Currency>)

    @Delete
    suspend fun deleteCurrency(user: Currency)

    @Query("SELECT * from Currency")
    fun getAllCurrencyList():  List<Currency>

    @Query("SELECT * from Currency where id = :id")
    fun observeCurrencyBId(id: Int): LiveData<Currency>
}