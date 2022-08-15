package com.example.zeeshanassignmentpaypay.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Currency")
data class Currency(
    var name: String? = null,
    var price: Double? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Int
) : Cloneable {

    constructor(name1: String, price1: Double) : this(name1, price1, 0) {
    }

    public override fun clone(): Currency {
        return Currency(name, price, 0)
    }
}
