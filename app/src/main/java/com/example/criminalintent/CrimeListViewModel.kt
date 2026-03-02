package com.example.criminalintent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CrimeListViewModel : ViewModel() {
    private val crimeRepository = CrimeRepository.get()

    private val _listeIncidents: MutableStateFlow<List<Crime>> = MutableStateFlow(emptyList())
    val listeIncidents: StateFlow<List<Crime>>
        get() = _listeIncidents.asStateFlow()

    init {
        viewModelScope.launch {
            crimeRepository.getIncidents().collect {
                _listeIncidents.value = it
            }
        }
    }

    suspend fun ajouterIncident(crime: Crime) {
        crimeRepository.ajouterIncident(crime)
    }
}