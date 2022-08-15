package com.example.zeeshanassignmentpaypay.data.model

data class Currency(var name: String? = null, var price: Double? = null) : Cloneable {
    public override fun clone(): Currency {
        return Currency(name, price)
    }
}
