package com.example.zeeshanassignmentpaypay.repository

import com.example.zeeshanassignmentpaypay.data.remote.api.ApiHelper
import com.example.zeeshanassignmentpaypay.data.model.Currency
import com.example.zeeshanassignmentpaypay.data.local.dao.CurrencyDao
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