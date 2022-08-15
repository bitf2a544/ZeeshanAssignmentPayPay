package com.example.zeeshanassignmentpaypay.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.zeeshanassignmentpaypay.data.model.Currency

@Database(
    entities = [Currency::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao
}