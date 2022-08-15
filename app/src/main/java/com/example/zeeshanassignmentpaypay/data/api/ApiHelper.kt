package com.example.zeeshanassignmentpaypay.data.api

import com.example.zeeshanassignmentpaypay.data.model.ExchangeRates
import retrofit2.Response

interface ApiHelper {
    suspend fun getLatestExchangeRatesFromAPI(apiKey: String?): Response<ExchangeRates>
}