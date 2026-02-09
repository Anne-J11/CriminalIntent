package com.example.criminalintent.bd

import androidx.room.TypeConverter
import java.util.Date

class ConvTypeIncident {
    @TypeConverter
    fun deDate(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun versDate(milliseconds: Long): Date {
        return Date()
    }
}