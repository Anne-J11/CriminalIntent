package com.example.criminalintent.bd

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.criminalintent.Crime

@Database(entities = [Crime::class], version = 2, exportSchema = true)
@TypeConverters(ConvTypeIncident::class)
abstract class BDIncident : RoomDatabase() {
    abstract fun crimeDAO(): CrimeDAO
}