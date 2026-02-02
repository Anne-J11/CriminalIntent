package com.example.criminalintent

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

private const val TAG = "CrimeListViewModel"

class CrimeListViewModel : ViewModel() {
    private val depotIncidents = CrimeRepository.get()
    val listeIncidents = mutableListOf<Crime>()

    init {
        Log.d(TAG, "Démarrage init")
        viewModelScope.launch {
            Log.d(TAG, "Démarrage coroutine")
            listeIncidents += chargerIncidents()
            Log.d(TAG, "Fin chargement liste d'incidents")
        }
    }

    suspend fun chargerIncidents() : List<Crime> {
        return depotIncidents.getIncidents()
    }

}