package com.example.criminalintent

import android.content.Context
import androidx.room.Room
import com.example.criminalintent.bd.BDIncident
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

private const val NOM_BD = "BDIncident"

class CrimeRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope
) {
    private val bd: BDIncident = Room
        .databaseBuilder(
            context.applicationContext,
            BDIncident::class.java,
            NOM_BD
        )
        //.createFromAsset("BDIncident")
        .fallbackToDestructiveMigration()
        .build()

    fun getIncidents(): Flow<List<Crime>> = bd.crimeDAO().getIncidents()

    suspend fun getIncident(id: UUID): Crime? = bd.crimeDAO().getIncident(id)

    fun majIncident(crime: Crime) {
        coroutineScope.launch {
            bd.crimeDAO().majIncident(crime)
        }
    }

    suspend fun ajouterIncident(crime: Crime) {
        bd.crimeDAO().ajouterIncident(crime)
    }

    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun initialiser(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository {
            return INSTANCE ?: throw IllegalStateException(
                "CrimeRepository doit d\'abord être initialisé"
            )
        }
    }
}