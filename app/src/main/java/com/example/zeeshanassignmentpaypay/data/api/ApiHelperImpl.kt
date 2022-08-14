package com.example.zeeshanassignmentpaypay.data.api

import com.example.zeeshanassignmentpaypay.data.model.ExchangeRates
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {

    override suspend fun getLatestExchangeRatesFromAPI(apiKey: String?): Response<ExchangeRates> =
        apiService.getLatestExchangeRates(apiKey)


}