package com.example.criminalintent

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.util.Date
import java.util.UUID

@Entity
data class Crime(
    @PrimaryKey val id: UUID,
    @ColumnInfo(name = "title") val titre: String,
    val date: Date,
    @ColumnInfo(name = "isSolved") val estResolu: Boolean  // ← isSolved et non isSalved
)