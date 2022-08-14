package com.example.zeeshanassignmentpaypay.data.api

import com.example.zeeshanassignmentpaypay.data.model.ExchangeRates
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("api/latest.json")
    suspend fun getLatestExchangeRates(
        @Query(
            "app_id",
            encoded = true
        ) app_id: String?
    ): Response<ExchangeRates>
}