package com.example.criminalintent

import androidx.lifecycle.ViewModel
import java.util.Date
import java.util.UUID

class CrimeListViewModel : ViewModel() {
    val listeIncidents = mutableListOf<Crime>()

    init {
        for (i in 0 until 100) {
            val incident = Crime(
                id = UUID.randomUUID(),
                titre = "Incident #$i",
                date = Date(),
                estResolu = i % 2 == 0
            )
            listeIncidents += incident
        }
    }
}