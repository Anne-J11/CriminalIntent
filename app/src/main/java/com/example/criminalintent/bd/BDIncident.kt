package com.example.criminalintent.bd

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.criminalintent.Crime

@Database(entities = [Crime::class], version = 2, exportSchema = true)
@TypeConverters(ConvTypeIncident::class)
abstract class BDIncident : RoomDatabase() {
    abstract fun crimeDAO(): CrimeDAO
}

val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(bd: SupportSQLiteDatabase){
        bd.execSQL(
            "ALTER TABLE Crime ADD COLUMN suspect TEXT NOT NULL DEFAULT ''"
        )
    }
}