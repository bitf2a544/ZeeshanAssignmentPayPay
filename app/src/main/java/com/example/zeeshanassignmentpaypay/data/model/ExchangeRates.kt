package com.example.zeeshanassignmentpaypay.data.model

import com.google.gson.annotations.SerializedName

data class ExchangeRates(
    @SerializedName("disclaimer") var disclaimer: String? = null,
    @SerializedName("license") var license: String? = null,
    @SerializedName("timestamp") var timestamp: Int? = null,
    @SerializedName("base") var base: String? = null,
    @SerializedName("rates") var rates: Rates? = Rates()
)