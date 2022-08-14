package com.example.zeeshanassignmentpaypay.data.repository

import com.example.zeeshanassignmentpaypay.data.api.ApiHelper
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper) {

    suspend fun getLatestExchangeRatesFromNetwork(apiKey: String?) =
        apiHelper.getLatestExchangeRatesFromAPI(apiKey)

}