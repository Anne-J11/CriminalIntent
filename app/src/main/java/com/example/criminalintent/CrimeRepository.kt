package com.example.criminalintent

import android.content.Context
import androidx.room.Room
import com.example.criminalintent.bd.BDIncident
import java.util.UUID
import kotlinx.coroutines.flow.Flow


private const val NOM_BD = "BDIncident"

class CrimeRepository private constructor(context: Context) {
    private val bd: BDIncident = Room
        .databaseBuilder(
            context.applicationContext,
            BDIncident::class.java,
            NOM_BD
        )
        .createFromAsset(NOM_BD)
        .build()
        .fallbackToDestructiveMigration()

    fun getIncidents(): Flow<List<Crime>> = bd.crimeDAO().getIncidents()
    suspend fun getIncident(id: UUID) = bd.crimeDAO().getIncident(id)
    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun initialiser(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository doit d'abord être initialisé")
        }
    }
}