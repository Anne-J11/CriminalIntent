package com.example.criminalintent.bd

import androidx.room.Dao
import androidx.room.Query
import com.example.criminalintent.Crime
import java.util.UUID

@Dao
interface CrimeDAO {
    @Query("SELECT * FROM crime")
    suspend fun chargerIncidents(): List<Crime>

    @Query("SELECT * FROM crime WHERE id=(:id)")
    suspend fun chargerIncident(id: UUID): Crime?
}