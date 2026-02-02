package com.example.criminalintent.bd

import androidx.room.TypeConverters
import java.util.Date

class ConvTypeIncident {
    @TypeConverters
    fun deDate(date: Date): Long{
        return date.time
    }
    @TypeConverters
    fun versDtae(date: Long): Date{
        return Date(date)
    }
}