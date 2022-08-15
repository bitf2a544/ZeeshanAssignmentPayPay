package com.example.zeeshanassignmentpaypay.data.repository

import com.example.zeeshanassignmentpaypay.data.api.ApiHelper
import com.example.zeeshanassignmentpaypay.data.model.Currency
import com.example.zeeshanassignmentpaypay.local.CurrencyDao
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val currencyDao: CurrencyDao
) {

    suspend fun getLatestExchangeRatesFromNetwork(apiKey: String?) =
        apiHelper.getLatestExchangeRatesFromAPI(apiKey)

    suspend fun getCurrencyListFromDatabase() = currencyDao.getAllCurrencyList()

    suspend fun insertCurrencyListInDatabase(currencyList:List<Currency>) = currencyDao.insertCurrencyList(currencyList)
}